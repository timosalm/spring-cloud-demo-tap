package com.example.gateway;

import brave.internal.collect.Lists;
import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfiguration {

    private static final Logger log = LoggerFactory.getLogger(OpenApiConfiguration.class);

    @Bean
    public OpenAPI openApi() {
        var securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("Spring Cloud Architecture API").version("1.0").description(""))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components().addSecuritySchemes(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
    @Bean
    public List<OpenApiCustomiser> openApiCustomiser(RouteDefinitionLocator locator, OpenAPI parentApi) {
        var routeDefinitions = locator.getRouteDefinitions().collectList().block();
        return routeDefinitions.stream()
                .filter(routeDefinition -> getGatewayPathPrefix(routeDefinition).startsWith("/services/"))
                .map(GroupCustomiser::new).collect(Collectors.toList());
    }

    private static String getGatewayPathPrefix(RouteDefinition routeDefinition) {
        return routeDefinition.getPredicates()
                .stream()
                .filter(predicateDefinition -> predicateDefinition.getName().equals("Path"))
                .findFirst()
                .map(predicateDefinition ->
                        predicateDefinition.getArgs().values().stream().findFirst().orElse("")
                                .replace("/**", ""))
                .orElse("");
    }

    private static class GroupCustomiser extends SpecFilter implements OpenApiCustomiser {
        private final RouteDefinition routeDefinition;

        public GroupCustomiser(RouteDefinition routeDefinition) {
            this.routeDefinition = routeDefinition;
        }

        @Override
        public void customise(OpenAPI parentApi) {
            var openApiUrl = routeDefinition.getUri() + "/v3/api-docs";
            log.info("GroupCustomiser called for OpenApi uri " + openApiUrl);
            var api = new OpenAPIV3Parser().read(openApiUrl);
            merge(parentApi, api);

            super.removeBrokenReferenceDefinitions(parentApi);
        }

        private void merge(OpenAPI parentApi, OpenAPI api) {
            parentApi.setTags(combineLists(parentApi.getTags(), api.getTags()));
            api.getPaths().forEach((name, item) -> {
                parentApi.getPaths().addPathItem(getGatewayPathPrefix(this.routeDefinition) + name, item);
            });
            parentApi.setExtensions(combineMaps(parentApi.getExtensions(), api.getExtensions()));
            merge(parentApi.getComponents(), api.getComponents());
        }

        private void merge(Components parentComponents, Components components) {
            parentComponents.setSchemas(combineMaps(parentComponents.getSchemas(), components.getSchemas()));
            parentComponents.setResponses(combineMaps(parentComponents.getResponses(), components.getResponses()));
            parentComponents.setParameters(combineMaps(parentComponents.getParameters(), components.getParameters()));
            parentComponents.setExamples(combineMaps(parentComponents.getExamples(), components.getExamples()));
            parentComponents.setRequestBodies(combineMaps(parentComponents.getRequestBodies(), components.getRequestBodies()));
            parentComponents.setHeaders(combineMaps(parentComponents.getHeaders(), components.getHeaders()));
            parentComponents.setSecuritySchemes(combineMaps(parentComponents.getSecuritySchemes(), components.getSecuritySchemes()));
            parentComponents.setLinks(combineMaps(parentComponents.getLinks(), components.getLinks()));
            parentComponents.setCallbacks(combineMaps(parentComponents.getCallbacks(), components.getCallbacks()));
            parentComponents.setExtensions(combineMaps(parentComponents.getExtensions(), components.getExtensions()));
        }

        private <K, V> Map<K, V> combineMaps(Map<K, V> map1, Map<K, V> map2) {
            if (map1 == null) return map2;
            if (map2 == null) return map1;
            return new LinkedHashMap<>() {{
                putAll(map1);
                putAll(map2);
            }};
        }

        private <K> List<K> combineLists(List<K> list1, List<K> list2) {
            if (list1 == null) return list2;
            if (list2 == null) return list1;
            return Lists.concat(list1, list2);
        }
    }

}
