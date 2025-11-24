package io.ipinfo.spring;

import io.ipinfo.api.IPinfoCore;
import io.ipinfo.api.model.IPResponseCore;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class IPinfoCoreSpring implements HandlerInterceptor {

    public static final String ATTRIBUTE_KEY =
        "IPinfoOfficialSparkWrapper.IPResponseCore";
    private final IPinfoCore ii;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    IPinfoCoreSpring(
        IPinfoCore ii,
        AttributeStrategy attributeStrategy,
        IPStrategy ipStrategy,
        InterceptorStrategy interceptorStrategy
    ) {
        this.ii = ii;
        this.attributeStrategy = attributeStrategy;
        this.ipStrategy = ipStrategy;
        this.interceptorStrategy = interceptorStrategy;
    }

    public static void main(String... args) {
        System.out.println(
            "This library is not meant to be run as a standalone jar."
        );
        System.exit(0);
    }

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) throws Exception {
        if (!interceptorStrategy.shouldRun(request)) {
            return true;
        }

        // Don't waste an API call if we already have it.
        // This should only happen for RequestAttributeStrategy and potentially
        // other implementations.
        if (attributeStrategy.hasCoreAttribute(request)) {
            return true;
        }

        String ip = ipStrategy.getIPAddress(request);
        if (ip == null) {
            return true;
        }

        IPResponseCore ipResponse = ii.lookupIP(ip);
        attributeStrategy.storeCoreAttribute(request, ipResponse);

        return true;
    }

    public static class Builder {

        private IPinfoCore ii = new IPinfoCore.Builder().build();
        private AttributeStrategy attributeStrategy =
            new SessionAttributeStrategy();
        private IPStrategy ipStrategy = new SimpleIPStrategy();
        private InterceptorStrategy interceptorStrategy =
            new BotInterceptorStrategy();

        public Builder setIPinfo(IPinfoCore ii) {
            this.ii = ii;
            return this;
        }

        public Builder attributeStrategy(AttributeStrategy attributeStrategy) {
            this.attributeStrategy = attributeStrategy;
            return this;
        }

        public Builder ipStrategy(IPStrategy ipStrategy) {
            this.ipStrategy = ipStrategy;
            return this;
        }

        public Builder interceptorStrategy(
            InterceptorStrategy interceptorStrategy
        ) {
            this.interceptorStrategy = interceptorStrategy;
            return this;
        }

        public IPinfoCoreSpring build() {
            return new IPinfoCoreSpring(
                ii,
                attributeStrategy,
                ipStrategy,
                interceptorStrategy
            );
        }
    }
}
