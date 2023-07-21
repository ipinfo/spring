package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPinfoSpring;

import jakarta.servlet.http.HttpServletRequest;

public class SessionAttributeStrategy implements AttributeStrategy {
    @Override
    public void storeAttribute(HttpServletRequest request, IPResponse response) {
        request.getSession().setAttribute(IPinfoSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponse getAttribute(HttpServletRequest request) {
        return (IPResponse) request.getSession().getAttribute(IPinfoSpring.ATTRIBUTE_KEY);
    }
}
