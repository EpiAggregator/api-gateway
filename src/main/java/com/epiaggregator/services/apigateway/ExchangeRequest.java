package com.epiaggregator.services.apigateway;

public class ExchangeRequest {
    private String accessToken;
    private String audience;

    public ExchangeRequest(String accessToken, String audience) {
        this.accessToken = accessToken;
        this.audience = audience;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }
}
