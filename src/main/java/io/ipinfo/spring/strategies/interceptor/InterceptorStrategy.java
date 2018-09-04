package io.ipinfo.spring.strategies.interceptor;

import javax.servlet.http.HttpServletRequest;

public interface InterceptorStrategy{

    boolean shouldRun(HttpServletRequest request);
}
