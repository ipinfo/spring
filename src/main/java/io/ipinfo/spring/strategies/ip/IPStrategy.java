package io.ipinfo.spring.strategies.ip;

import jakarta.servlet.http.HttpServletRequest;

public interface IPStrategy {
    /**
     * Gets the IP Address from the request
     *
     * @param request that originated from a client to this server
     * @return the ip address
     */
    String getIPAddress(HttpServletRequest request);
}
