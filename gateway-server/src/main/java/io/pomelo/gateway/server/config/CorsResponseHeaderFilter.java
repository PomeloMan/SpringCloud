package io.pomelo.gateway.server.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.collect.Lists;

import reactor.core.publisher.Mono;

/**
 * 解决 The 'Access-Control-Allow-Origin' header contains multiple values "*", "*"
 * 的问题
 * 
 * @author FengChao
 */
@Component("corsResponseHeaderFilter")
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

	@Override
	public int getOrder() {
		return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		return chain.filter(exchange).then(Mono.defer(() -> {
			exchange.getResponse().getHeaders().entrySet().stream()
					.filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
					.filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
							|| kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)))
					.forEach(kv -> {
						kv.setValue(Lists.newArrayList(kv.getValue().get(0)));
					});

			return chain.filter(exchange);
		}));
	}
}
