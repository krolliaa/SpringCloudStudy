package com.kk.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
//@Order(value = -1)
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //获取第一个匹配的 authorization 参数即可
        String authorization = exchange.getRequest().getQueryParams().getFirst("authorization");
        //对参数进行判断，如果是 admin 就放行，否则不放行
        if ("admin".equals(authorization)) return chain.filter(exchange);
        //设置状态码，拦截返回
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    /**
     * 设置过滤器优先级，值越低优先级越高 ---> 也可以使用 @Order 注解
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
