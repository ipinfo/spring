package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPInfoSpring;

import javax.servlet.http.HttpServletRequest;

public class RequestAttributeStrategy implements AttributeStrategy {
    @Override
    public void storeAttribute(HttpServletRequest request, IPResponse response) {
        request.setAttribute(IPInfoSpring.ATTRIBUTE_KEY, response);
    }
}
