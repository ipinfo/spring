package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;

import javax.servlet.http.HttpServletRequest;

public interface AttributeStrategy {

    void storeAttribute(HttpServletRequest request, IPResponse response);

    IPResponse getAttribute(HttpServletRequest request);
}
