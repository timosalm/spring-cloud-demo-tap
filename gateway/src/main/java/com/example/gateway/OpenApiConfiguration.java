package com.example.gateway;

import io.swagger.v3.core.filter.SpecFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springdoc.core.OpenAPIService;
import org.springdoc.core.customizers.OpenApiBuilderCustomizer;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("Spring Cloud Architecture API").version("1.0").description(""));
    }

    @Bean
    public List<OpenApiBuilderCustomizer> openApiCustomizer(RouteDefinitionLocator locator, OpenAPI openApi) {
        return Objects.requireNonNull(
                        locator.getRouteDefinitions().collectList().block())
                .stream()
                .map(routeDefinition -> new GroupCustomiser(routeDefinition, openApi))
                .collect(Collectors.toList());
    }

    private class GroupCustomiser extends SpecFilter implements OpenApiBuilderCustomizer {
        private RouteDefinition routeDefinition;
        private OpenAPI openApi;

        public GroupCustomiser(RouteDefinition routeDefinition, OpenAPI openApi) {
            this.routeDefinition = routeDefinition;
            this.openApi = openApi;
        }

        @Override
        public void customise(OpenAPIService openApiService) {
            var api = new OpenAPIV3Parser().read(routeDefinition.getUri() + "/v3/api-docs");
            this.openApi.setComponents(api.getComponents());
            openApi.setPaths(api.getPaths());
            openApi.setExtensions(api.getExtensions());
            openApi.setInfo(api.getInfo());
            openApi.setExternalDocs(api.getExternalDocs());
            openApi.setOpenapi(api.getOpenapi());
            openApi.setSecurity(api.getSecurity());
            openApi.setServers(api.getServers());
            openApi.setTags(api.getTags());
            super.removeBrokenReferenceDefinitions(openApi);
        }
    }

}
