package io.ipinfo.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import io.ipinfo.spring.strategies.interceptor.InterceptorStrategy;
import io.ipinfo.spring.strategies.ip.IPStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@ExtendWith(MockitoExtension.class)
class IPinfoSpringTest {

    @Mock
    private IPinfo mockIPinfoClient;

    @Mock
    private AttributeStrategy mockAttributeStrategy;

    @Mock
    private IPStrategy mockIpStrategy;

    @Mock
    private InterceptorStrategy mockInterceptorStrategy;

    @InjectMocks
    private IPinfoSpring ipinfoSpring;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private Object handler;

    private IPResponse dummyIPResponse;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        handler = new Object();

        dummyIPResponse = new IPResponse(
            "8.8.8.8",
            "dns.google",
            false,
            false,
            false,
            "Mountain View",
            "CA",
            "United States",
            "37.40599,-122.07851",
            "AS15169 Google LLC",
            "94043",
            "America/Los_Angeles",
            null,
            null,
            null,
            null,
            null,
            null
        );
    }

    @Test
    @DisplayName("should skip processing if interceptorStrategy returns false")
    void preHandle_shouldSkipIfInterceptorStrategyFalse() throws Exception {
        when(mockInterceptorStrategy.shouldRun(request)).thenReturn(false);

        boolean result = ipinfoSpring.preHandle(request, response, handler);

        assertTrue(result, "preHandle should return true to continue chain");
        // Verify that no other strategies were called if shouldRun returned false
        verify(mockInterceptorStrategy).shouldRun(request);
        verifyNoInteractions(
            mockAttributeStrategy,
            mockIpStrategy,
            mockIPinfoClient
        );
    }

    @Test
    @DisplayName(
        "should skip processing if attributeStrategy already has attribute"
    )
    void preHandle_shouldSkipIfHasAttribute() throws Exception {
        when(mockInterceptorStrategy.shouldRun(request)).thenReturn(true);
        when(mockAttributeStrategy.hasAttribute(request)).thenReturn(true);

        boolean result = ipinfoSpring.preHandle(request, response, handler);

        assertTrue(result, "preHandle should return true to continue chain");
        verify(mockInterceptorStrategy).shouldRun(request);
        verify(mockAttributeStrategy).hasAttribute(request);
        // Verify no IP lookup or storage occurred
        verifyNoInteractions(mockIpStrategy, mockIPinfoClient);
    }

    @Test
    @DisplayName("should skip processing if IPStrategy returns null IP")
    void preHandle_shouldSkipIfIpIsNull() throws Exception {
        when(mockInterceptorStrategy.shouldRun(request)).thenReturn(true);
        when(mockAttributeStrategy.hasAttribute(request)).thenReturn(false);
        when(mockIpStrategy.getIPAddress(request)).thenReturn(null);

        boolean result = ipinfoSpring.preHandle(request, response, handler);

        assertTrue(result, "preHandle should return true to continue chain");
        verify(mockInterceptorStrategy).shouldRun(request);
        verify(mockAttributeStrategy).hasAttribute(request);
        verify(mockIpStrategy).getIPAddress(request);
        // Verify no IP lookup or storage occurred
        verifyNoInteractions(mockIPinfoClient);
        verify(mockAttributeStrategy, never()).storeAttribute(any(), any());
    }

    @Test
    @DisplayName(
        "should perform IP lookup and store attribute if all conditions met"
    )
    void preHandle_shouldProcessAndStore() throws Exception {
        String testIp = "8.8.8.8";
        when(mockInterceptorStrategy.shouldRun(request)).thenReturn(true);
        when(mockAttributeStrategy.hasAttribute(request)).thenReturn(false);
        when(mockIpStrategy.getIPAddress(request)).thenReturn(testIp);
        when(mockIPinfoClient.lookupIP(testIp)).thenReturn(dummyIPResponse);

        boolean result = ipinfoSpring.preHandle(request, response, handler);

        assertTrue(result, "preHandle should return true to continue chain");
        verify(mockInterceptorStrategy).shouldRun(request);
        verify(mockAttributeStrategy).hasAttribute(request);
        verify(mockIpStrategy).getIPAddress(request);
        verify(mockIPinfoClient).lookupIP(testIp);
        verify(mockAttributeStrategy).storeAttribute(request, dummyIPResponse);
    }

    @Test
    @DisplayName("should rethrow RateLimitedException during lookup")
    void preHandle_shouldRethrowRateLimitedException() throws Exception {
        String testIp = "invalid.ip";
        when(mockInterceptorStrategy.shouldRun(request)).thenReturn(true);
        when(mockAttributeStrategy.hasAttribute(request)).thenReturn(false);
        when(mockIpStrategy.getIPAddress(request)).thenReturn(testIp);
        // Simulate a RateLimitedException during lookup
        when(mockIPinfoClient.lookupIP(testIp)).thenThrow(
            new RateLimitedException()
        );

        assertThrows(RateLimitedException.class, () ->
            ipinfoSpring.preHandle(request, response, handler)
        );

        verify(mockInterceptorStrategy).shouldRun(request);
        verify(mockAttributeStrategy).hasAttribute(request);
        verify(mockIpStrategy).getIPAddress(request);
        verify(mockIPinfoClient).lookupIP(testIp);
        verify(mockAttributeStrategy, never()).storeAttribute(any(), any());
    }
}
