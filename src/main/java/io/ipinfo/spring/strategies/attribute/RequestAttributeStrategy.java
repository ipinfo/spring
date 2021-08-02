package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPinfoSpring;

import javax.servlet.http.HttpServletRequest;

public class RequestAttributeStrategy implements AttributeStrategy {
    @Override
    public void storeAttribute(HttpServletRequest request, IPResponse response) {
        request.setAttribute(IPinfoSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponse getAttribute(HttpServletRequest request) {
        return (IPResponse) request.getAttribute(IPinfoSpring.ATTRIBUTE_KEY);
    }
}
