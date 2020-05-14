package io.pomelo.user.center.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import io.pomelo.user.center.core.service.UserService;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

	//RSA配置
    @Value("${jwt.token.signing-key}")
    private String signingKey;
    @Value("${jwt.token.verifier-key}")
    private String verifierKey;

	@Autowired
	DataSource datasource;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * GrantTypes
	 * 1) authorization_code -> 授权码模式(先登录获取code，再通过code获取token)
	 * 		http://localhost:8080/oauth/authorize?response_type=code&client_id=client_id&redirect_uri=http://baidu.com
	 * 		http://localhost:8080/oauth/token?grant_type=authorization_code&code=o4YrCS&client_id=client_id&client_secret=client_secret&redirect_uri=http://baidu.com
	 * 2) password -> 密码模式(通过用户名密码获取token)
	 *		http://localhost:8080/oauth/token
	 * 		{ client_id: client_id, client_secret: client_secret, grant_type: password, username: username, password: password }
	 * 3) client_credentials -> 客户端模式(用户向客户端注册，然后客户端以自己的名义向服务端获取资源)
	 * 		http://localhost:8080/oauth/token
	 * 		{ client_id: client_id, client_secret: client_secret, grant_type: client_credentials }
	 * 4) implicit -> 简化模式(在redirect_uri的Hash传递token，认证客户端运行在浏览器中 如JS，Flash)
	 * 5) refresh_token -> 刷新access_token
	 * 		http://localhost:8080/oauth/token
	 * 		{ refresh_token: refresh_token, client_id: client_id, client_secret: client_secret, grant_type: refresh_token, username: username, password: password }
	 * 		{ refresh_token: refresh_token, client_id: client_id, client_secret: client_secret, grant_type: refresh_token }
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() // 基于内存
				.withClient("springcloud") // 授权客户端
				.secret(passwordEncoder.encode("springcloud")) // 授权码
				.accessTokenValiditySeconds(3600)
				.authorizedGrantTypes("refresh_token", "password", "client_credentials", "authorization_code") // 授权模式
				.scopes("all", "public") // 授权范围
				.and()
				.withClient("gateway")
				.secret(passwordEncoder.encode("gateway"))
				.accessTokenValiditySeconds(3600)
				.authorizedGrantTypes("refresh_token", "client_credentials", "authorization_code")
				.scopes("all");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
				.userDetailsService(userService) // DaoAuthenticationProvider
				.tokenStore(new InMemoryTokenStore())
				.accessTokenConverter(accessTokenConverter());
	}

	/**
	 * ClientCredentialsTokenEndpointFilter。class
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.allowFormAuthenticationForClients()
				// 开启/oauth/token_key验证端口无权限访问
				.tokenKeyAccess("permitAll()")
				// 开启/oauth/check_token验证端口认证权限访问(CheckTokenEndpoint.class，TokenEndpoint.class)
				.checkTokenAccess("isAuthenticated()");
//				.addTokenEndpointAuthenticationFilter(new TokenEndpointAuthenticationFilter());
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey(signingKey);
		converter.setVerifierKey(verifierKey);
		return converter;
	}
}
