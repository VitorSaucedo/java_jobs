package com.vitorsaucedo.buscadorjava.config;

import com.vitorsaucedo.buscadorjava.client.AdzunaClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AdzunaConfig {

    @Value("${adzuna.api.base-url}")
    private String baseUrl;

    @Bean
    public RestClient adzunaRestClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean
    public AdzunaClient adzunaClient(RestClient restClient) {
        RestClientAdapter adapter = RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AdzunaClient.class);
    }
}
