package io.ipinfo.spring.strategies.ip;

import jakarta.servlet.http.HttpServletRequest;

public class XForwardedForIPStrategy implements IPStrategy {
    /**
     * Gets the ip address using the X-Forwarded-For header.
     *
     * @param request originated from a client to this server.
     * @return
     */
    @Override
    public String getIPAddress(HttpServletRequest request) {
        String XforwardedFor = request.getHeader("X-Forwarded-For");
        if (XforwardedFor != null) {
            return XforwardedFor.split(",", 0)[0];
        }
        return request.getRemoteAddr();
    }
}
