package io.ipinfo.spring.strategies.interceptor;

import jakarta.servlet.http.HttpServletRequest;

public interface InterceptorStrategy{
    boolean shouldRun(HttpServletRequest request);
}
