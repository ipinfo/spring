package io.ipinfo.spring;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IPInfoSpring extends HandlerInterceptorAdapter {
    public static final String ATTRIBUTE_KEY = "IPinfoOfficialSparkWrapper.IPResponse";
    private final IPInfo ipInfo;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    IPInfoSpring(IPInfo ipInfo, AttributeStrategy attributeStrategy, IPStrategy ipStrategy, InterceptorStrategy interceptorStrategy) {
        this.ipInfo = ipInfo;
        this.attributeStrategy = attributeStrategy;
        this.ipStrategy = ipStrategy;
        this.interceptorStrategy = interceptorStrategy;
    }

    public static void main(String... args) {
        System.out.println("This library is not meant to be run as a standalone jar.");

        System.exit(0);
    }

    public static IPInfoSpringBuilder builder() {
        return new IPInfoSpringBuilder();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!interceptorStrategy.shouldRun(request)) return true;

        // Don't waste an API call if we already have it.
        // This should only happen for RequestAttributeStrategy and potentially other implementations.
        if (attributeStrategy.hasAttribute(request)) return true;

        String ip = ipStrategy.getIPAddress(request);
        if (ip == null) return true;


        IPResponse ipResponse = ipInfo.lookupIP(ip);
        attributeStrategy.storeAttribute(request, ipResponse);

        return true;
    }
}
