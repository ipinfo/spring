package io.ipinfo.spring.strategies.ip;

import javax.servlet.http.HttpServletRequest;

public class SimpleIPStrategy implements IPStrategy {
    /**
     * Gets the ip address using the RemoteAddress the server sees
     * This will fail behind reverse proxies or CDNs.
     *
     * @param request originated from a client to this server.
     * @return
     */
    @Override
    public String getIPAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
