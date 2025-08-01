package io.ipinfo.spring;

import io.ipinfo.api.IPinfoLite;
import io.ipinfo.api.model.IPResponseLite;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class IPinfoLiteSpring implements HandlerInterceptor {

    public static final String ATTRIBUTE_KEY =
        "IPinfoOfficialSparkWrapper.IPResponseLite";
    private final IPinfoLite ii;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    IPinfoLiteSpring(
        IPinfoLite ii,
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
        if (attributeStrategy.hasLiteAttribute(request)) {
            return true;
        }

        String ip = ipStrategy.getIPAddress(request);
        if (ip == null) {
            return true;
        }

        IPResponseLite ipResponse = ii.lookupIP(ip);
        attributeStrategy.storeLiteAttribute(request, ipResponse);

        return true;
    }

    public static class Builder {

        private IPinfoLite ii = new IPinfoLite.Builder().build();
        private AttributeStrategy attributeStrategy =
            new SessionAttributeStrategy();
        private IPStrategy ipStrategy = new SimpleIPStrategy();
        private InterceptorStrategy interceptorStrategy =
            new BotInterceptorStrategy();

        public Builder setIPinfo(IPinfoLite ii) {
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

        public IPinfoLiteSpring build() {
            return new IPinfoLiteSpring(
                ii,
                attributeStrategy,
                ipStrategy,
                interceptorStrategy
            );
        }
    }
}
