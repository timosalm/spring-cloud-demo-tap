package com.example.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class RewriteResponseBodyGatewayFilterFactory extends AbstractGatewayFilterFactory<RewriteResponseBodyGatewayFilterFactory.Config> {

    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory;

    public RewriteResponseBodyGatewayFilterFactory(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory) {
        super(Config.class);
        this.modifyResponseBodyFilterFactory = modifyResponseBodyFilterFactory;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return modifyResponseBodyFilterFactory.apply(c -> c.setRewriteFunction(String.class, String.class, (serverWebExchange, body) -> {
            config.getRewritesMap().forEach(body::replaceAll);
            return Mono.just(body);
        }));
    }

    public static class Config {
        private String rewritesString;

        public Config() {
        }

        public String getRewritesString() {
            return rewritesString;
        }

        public void setRewritesString(String rewritesString) {
            this.rewritesString = rewritesString;
        }

        public Map<String, String> getRewritesMap() {
            var rewritesMap = new HashMap<String,String>();

            for (String rewrite: rewritesString.split(",")) {
                rewritesMap.put(rewrite.split(":")[0], rewrite.split(":")[1]);
            }
            return rewritesMap;
        }

    }
}