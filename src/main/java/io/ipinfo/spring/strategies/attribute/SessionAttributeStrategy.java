package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.model.IPResponseCore;
import io.ipinfo.api.model.IPResponseLite;
import io.ipinfo.spring.IPinfoCoreSpring;
import io.ipinfo.spring.IPinfoLiteSpring;
import io.ipinfo.spring.IPinfoSpring;
import jakarta.servlet.http.HttpServletRequest;

public class SessionAttributeStrategy implements AttributeStrategy {

    @Override
    public void storeAttribute(
        HttpServletRequest request,
        IPResponse response
    ) {
        request.getSession().setAttribute(IPinfoSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponse getAttribute(HttpServletRequest request) {
        return (IPResponse) request
            .getSession()
            .getAttribute(IPinfoSpring.ATTRIBUTE_KEY);
    }

    @Override
    public void storeLiteAttribute(
        HttpServletRequest request,
        IPResponseLite response
    ) {
        request
            .getSession()
            .setAttribute(IPinfoLiteSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponseLite getLiteAttribute(HttpServletRequest request) {
        return (IPResponseLite) request
            .getSession()
            .getAttribute(IPinfoLiteSpring.ATTRIBUTE_KEY);
    }

    @Override
    public void storeCoreAttribute(
        HttpServletRequest request,
        IPResponseCore response
    ) {
        request
            .getSession()
            .setAttribute(IPinfoCoreSpring.ATTRIBUTE_KEY, response);
    }

    @Override
    public IPResponseCore getCoreAttribute(HttpServletRequest request) {
        return (IPResponseCore) request
            .getSession()
            .getAttribute(IPinfoCoreSpring.ATTRIBUTE_KEY);
    }
}
