package io.pomelo.commons;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigure implements WebMvcConfigurer {

	@Value("$#{'${cors.exposed-headers}'.split(',')}")
	private List<String> exposedHeaders;
	@Value("#{'${cors.allowed-origins}'.split(',')}")
	private List<String> allowedOrigins;

	/**
	 * <p>
	 * 跨域设置<br/>
	 * Cross-domain setting
	 * </p>
	 * {@link https://spring.io/blog/2015/06/08/cors-support-in-spring-framework}
	 * {@link https://spring.io/guides/gs/rest-service-cors/}
	 * {@link application.properties/endpoints.cors.allowed-xxx}
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				// Set exposed header for front page to get the header.
				.exposedHeaders(exposedHeaders.toArray(new String[exposedHeaders.size()]))
				.allowedOrigins(allowedOrigins.toArray(new String[allowedOrigins.size()]))
				.allowedMethods("GET", "POST", "OPTIONS", "PUT", "DELETE", "*")
				// .allowedHeaders("Content-Type", "X-Requested-With", "accept", "Origin",
				// "Access-Control-Request-Method", "Access-Control-Request-Headers", "*")
				.allowCredentials(true).maxAge(3600);
	}

}
