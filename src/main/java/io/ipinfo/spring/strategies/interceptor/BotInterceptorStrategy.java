package io.ipinfo.spring.strategies.interceptor;

import javax.servlet.http.HttpServletRequest;

public class BotInterceptorStrategy implements InterceptorStrategy {

    @Override
    public boolean shouldRun(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) return false;

        userAgent = userAgent.toLowerCase();
        if (userAgent.contains("spider") || userAgent.contains("bot")) return false;

        return true;
    }
}
