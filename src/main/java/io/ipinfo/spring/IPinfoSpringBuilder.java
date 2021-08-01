package io.ipinfo.spring;

import io.ipinfo.api.IPinfo;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.SessionAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;

public class IPinfoSpringBuilder {
    private IPinfo ipInfo = new IPinfo.Builder().build();
    private AttributeStrategy attributeStrategy = new SessionAttributeStrategy();
    private IPStrategy ipStrategy = new SimpleIPStrategy();
    private InterceptorStrategy interceptorStrategy = new BotInterceptorStrategy();

    public IPinfoSpringBuilder ipInfo(IPinfo ipInfo) {
        this.ipInfo = ipInfo;
        return this;
    }

    public IPinfoSpringBuilder attributeStrategy(AttributeStrategy attributeStrategy) {
        this.attributeStrategy = attributeStrategy;
        return this;
    }

    public IPinfoSpringBuilder ipStrategy(IPStrategy ipStrategy) {
        this.ipStrategy = ipStrategy;
        return this;
    }

    public IPinfoSpringBuilder interceptorStrategy(InterceptorStrategy interceptorStrategy) {
        this.interceptorStrategy = interceptorStrategy;
        return this;
    }

    public IPinfoSpring build() {
        return new IPinfoSpring(ipInfo, attributeStrategy, ipStrategy, interceptorStrategy);
    }
}
