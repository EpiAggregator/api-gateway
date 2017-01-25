package com.epiaggregator.services.apigateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("auth-service")
public interface AuthClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/v1/token/exchange",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    ExchangeResponse exchange(ExchangeRequest exchangeRequest);
}
