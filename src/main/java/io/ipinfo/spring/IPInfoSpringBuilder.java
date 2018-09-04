package io.ipinfo.spring;

import io.ipinfo.api.IPInfo;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.attribute.RequestAttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.BotInterceptorStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import io.ipinfo.spring.strategies.ip.SimpleIPStrategy;

public class IPInfoSpringBuilder {
    private IPInfo ipInfo = IPInfo.builder().build();
    private AttributeStrategy attributeStrategy = new RequestAttributeStrategy();
    private IPStrategy ipStrategy = new SimpleIPStrategy();
    private InterceptorStrategy interceptorStrategy = new BotInterceptorStrategy();

    public IPInfoSpringBuilder setIpInfo(IPInfo ipInfo) {
        this.ipInfo = ipInfo;
        return this;
    }

    public IPInfoSpringBuilder setAttributeStrategy(AttributeStrategy attributeStrategy) {
        this.attributeStrategy = attributeStrategy;
        return this;
    }

    public IPInfoSpringBuilder setIpStrategy(IPStrategy ipStrategy) {
        this.ipStrategy = ipStrategy;
        return this;
    }

    public IPInfoSpringBuilder setInterceptorStrategy(InterceptorStrategy interceptorStrategy) {
        this.interceptorStrategy = interceptorStrategy;
        return this;
    }

    public IPInfoSpring build() {
        return new IPInfoSpring(ipInfo, attributeStrategy, ipStrategy, interceptorStrategy);
    }
}
