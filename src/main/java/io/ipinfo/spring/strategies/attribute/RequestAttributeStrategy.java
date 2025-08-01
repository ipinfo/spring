package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.model.IPResponseLite;
import io.ipinfo.spring.IPinfoLiteSpring;
import io.ipinfo.spring.IPinfoSpring;
import jakarta.servlet.http.HttpServletRequest;

public class RequestAttributeStrategy implements AttributeStrategy {

    @Override
    public void storeAttribute(
        HttpServletRequest request,
        IPResponse response
    ) {
        request.setAttribute(IPinfoSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponse getAttribute(HttpServletRequest request) {
        return (IPResponse) request.getAttribute(IPinfoSpring.ATTRIBUTE_KEY);
    }

    @Override
    public void storeLiteAttribute(
        HttpServletRequest request,
        IPResponseLite response
    ) {
        request.setAttribute(IPinfoLiteSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponseLite getLiteAttribute(HttpServletRequest request) {
        return (IPResponseLite) request.getAttribute(
            IPinfoLiteSpring.ATTRIBUTE_KEY
        );
    }
}
