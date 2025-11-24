package io.ipinfo.spring.strategies.attribute;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.api.model.IPResponseCore;
import io.ipinfo.api.model.IPResponseLite;
import jakarta.servlet.http.HttpServletRequest;

public interface AttributeStrategy {
    void storeAttribute(HttpServletRequest request, IPResponse response);

    IPResponse getAttribute(HttpServletRequest request);

    default boolean hasAttribute(HttpServletRequest request) {
        if (getAttribute(request) != null) {
            return true;
        }

        return false;
    }

    default void storeLiteAttribute(
        HttpServletRequest request,
        IPResponseLite response
    ) {
        throw new UnsupportedOperationException(
            "This strategy does not support IPResponseLite."
        );
    }

    default IPResponseLite getLiteAttribute(HttpServletRequest request) {
        throw new UnsupportedOperationException(
            "This strategy does not support IPResponseLite."
        );
    }

    default boolean hasLiteAttribute(HttpServletRequest request) {
        return getLiteAttribute(request) != null;
    }

    default void storeCoreAttribute(
        HttpServletRequest request,
        IPResponseCore response
    ) {
        throw new UnsupportedOperationException(
            "This strategy does not support IPResponseCore."
        );
    }

    default IPResponseCore getCoreAttribute(HttpServletRequest request) {
        throw new UnsupportedOperationException(
            "This strategy does not support IPResponseCore."
        );
    }

    default boolean hasCoreAttribute(HttpServletRequest request) {
        return getCoreAttribute(request) != null;
    }
}
