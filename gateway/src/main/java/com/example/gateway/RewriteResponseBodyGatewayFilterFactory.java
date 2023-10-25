package com.example.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
            for (Map.Entry<String, String> entry:  config.getRewritesMap().entrySet()) {
                body = body.replaceAll(entry.getKey(), entry.getValue());
            }
            return Mono.just(body);
        }));
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("rewrites");
    }

    public static class Config {

        private List<String> rewrites;

        public void setRewrites(List<String> rewrites) {
            this.rewrites = rewrites;
        }

        public Map<String, String> getRewritesMap() {
            var rewritesMap = new HashMap<String, String>();
            rewrites.forEach(rewrite -> rewritesMap.put(rewrite.split(":")[0], rewrite.split(":")[1]));
            return rewritesMap;
        }
    }
}