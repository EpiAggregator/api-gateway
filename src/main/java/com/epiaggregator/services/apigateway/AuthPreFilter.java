package com.epiaggregator.services.apigateway;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AuthPreFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(AuthPreFilter.class);

    @Autowired
    private AuthClient authClient;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        return !("POST".equals(request.getMethod()) && (
                request.getRequestURI().contains("/v1/users") ||
                        request.getRequestURI().contains("/v1/token")));
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));

        String header = request.getHeader("Authorization");
        if (header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
            ctx.setResponseStatusCode(401);
            ctx.setSendZuulResponse(false);
        } else {
            header = header.replace("Bearer ", "");
            if ("".equals(header)) {
                ctx.setResponseStatusCode(401);
                ctx.setSendZuulResponse(false);
                return null;
            }
            ExchangeResponse exchangeResponse = authClient.exchange(new ExchangeRequest(header, "*"));
            ctx.addZuulRequestHeader("Authorization", "Bearer " + exchangeResponse.getJwtUser());
        }
        return null;
    }
}
