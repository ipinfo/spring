package io.ipinfo.spring;

import io.ipinfo.api.IPInfo;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;

public class IPInfoSpringBuilder {
    private IPInfo ipInfo = IPInfo.builder().build();
    private AttributeStrategy attributeStrategy = new SessionAttributeStrategy();
    private IPStrategy ipStrategy = new SimpleIPStrategy();
    private InterceptorStrategy interceptorStrategy = new BotInterceptorStrategy();

    public IPInfoSpringBuilder ipInfo(IPInfo ipInfo) {
        this.ipInfo = ipInfo;
        return this;
    }

    public IPInfoSpringBuilder attributeStrategy(AttributeStrategy attributeStrategy) {
        this.attributeStrategy = attributeStrategy;
        return this;
    }

    public IPInfoSpringBuilder ipStrategy(IPStrategy ipStrategy) {
        this.ipStrategy = ipStrategy;
        return this;
    }

    public IPInfoSpringBuilder interceptorStrategy(InterceptorStrategy interceptorStrategy) {
        this.interceptorStrategy = interceptorStrategy;
        return this;
    }

    public IPInfoSpring build() {
        return new IPInfoSpring(ipInfo, attributeStrategy, ipStrategy, interceptorStrategy);
    }
}
