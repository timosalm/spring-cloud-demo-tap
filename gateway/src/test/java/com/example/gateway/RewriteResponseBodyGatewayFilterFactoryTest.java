package com.example.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = RewriteResponseBodyGatewayFilterFactoryTest.RewriteResponseBodyGatewayFilterFactoryTestConfig.class)
@AutoConfigureWebTestClient
class RewriteResponseBodyGatewayFilterFactoryTest {

    @Autowired
    WireMockServer wireMockServer;

    @Autowired
    WebTestClient webTestClient;

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void shouldAddHeaderWithComputedHash() {
        wireMockServer.stubFor(WireMock.get("/post").willReturn(WireMock.ok("ISSUER_HOST  CLIENT_ID_VALUE")));

        var result = webTestClient.get().uri("/frontend/post")
                .exchange()
                .returnResult(String.class).getResponseBody().blockFirst();

        System.out.println(result);
    }

    @TestConfiguration
    static class RewriteResponseBodyGatewayFilterFactoryTestConfig {

        @Bean(destroyMethod = "stop")
        WireMockServer wireMockServer() {
            WireMockConfiguration options = wireMockConfig().port(8009);
            WireMockServer wireMock = new WireMockServer(options);
            wireMock.start();
            return wireMock;
        }
    }
}