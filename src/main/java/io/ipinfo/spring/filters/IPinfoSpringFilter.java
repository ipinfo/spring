package io.ipinfo.spring.filters;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

public class IPinfoSpringFilter implements Filter {
    private static final Logger logger = Logger.getLogger(IPinfoSpringFilter.class.getName());

    private final IPinfo ii;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    public IPinfoSpringFilter(IPinfo ii, AttributeStrategy attributeStrategy, IPStrategy ipStrategy, InterceptorStrategy interceptorStrategy) {
        this.ii = ii;
        this.attributeStrategy = attributeStrategy;
        this.ipStrategy = ipStrategy;
        this.interceptorStrategy = interceptorStrategy;
    }

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("Initializing IPinfoSpringFilter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (!interceptorStrategy.shouldRun(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        if (attributeStrategy.hasAttribute(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        String ip = ipStrategy.getIPAddress(httpRequest);
        if (ip == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            IPResponse ipResponse = ii.lookupIP(ip);
            attributeStrategy.storeAttribute(httpRequest, ipResponse);
        } catch (Exception e) {
            logger.severe("Error while fetching IP information: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("Destroying IPinfoSpringFilter");
    }
}
