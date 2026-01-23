package io.ipinfo.spring;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.ResproxyResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class IPinfoResproxySpring implements HandlerInterceptor {

    public static final String ATTRIBUTE_KEY =
        "IPinfoOfficialSparkWrapper.ResproxyResponse";
    private final IPinfo ii;
    private final AttributeStrategy attributeStrategy;
    private final IPStrategy ipStrategy;
    private final InterceptorStrategy interceptorStrategy;

    IPinfoResproxySpring(
        IPinfo ii,
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
        if (attributeStrategy.hasResproxyAttribute(request)) {
            return true;
        }

        String ip = ipStrategy.getIPAddress(request);
        if (ip == null) {
            return true;
        }

        ResproxyResponse resproxyResponse = ii.lookupResproxy(ip);
        attributeStrategy.storeResproxyAttribute(request, resproxyResponse);

        return true;
    }

    public static class Builder {

        private IPinfo ii = new IPinfo.Builder().build();
        private AttributeStrategy attributeStrategy =
            new SessionAttributeStrategy();
        private IPStrategy ipStrategy = new SimpleIPStrategy();
        private InterceptorStrategy interceptorStrategy =
            new BotInterceptorStrategy();

        public Builder setIPinfo(IPinfo ii) {
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

        public IPinfoResproxySpring build() {
            return new IPinfoResproxySpring(
                ii,
                attributeStrategy,
                ipStrategy,
                interceptorStrategy
            );
        }
    }
}
