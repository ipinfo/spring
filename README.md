# [<img src="https://ipinfo.io/static/ipinfo-small.svg" alt="IPinfo" width="24"/>](https://ipinfo.io/) IPinfo Spring Client Library

[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)

This is the official Spring client library for the IPinfo.io IP address API,
allowing you to look up your own IP address, or get any of the following details
for an IP:

 - [IP geolocation data](https://ipinfo.io/ip-geolocation-api) (city, region, country, postal code, latitude, and longitude)
 - [ASN information](https://ipinfo.io/asn-api) (ISP or network operator, associated domain name, and type, such as business, hosting, or company)
 - [Company data](https://ipinfo.io/ip-company-api) (the name and domain of the business that uses the IP address)
 - [Carrier details](https://ipinfo.io/ip-carrier-api) (the name of the mobile carrier and MNC and MCC for that carrier if the IP is used exclusively for mobile traffic)

Check all the data we have for your IP address [here](https://ipinfo.io/what-is-my-ip).

## Getting Started

You'll need an IPinfo API access token, which you can get by signing up for a
free account at [https://ipinfo.io/signup](https://ipinfo.io/signup).

The free plan is limited to 50,000 requests per month, and doesn't include some
of the data fields such as IP type and company data. To enable all the data
fields and additional request volumes see
[https://ipinfo.io/pricing](https://ipinfo.io/pricing)

[Click here to view the Java Spring SDK's API documentation](https://ipinfo.github.io/spring/).

The library also supports the Lite API, see the [Lite API section](#lite-api) for more info.

## Usage

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.ipinfo</groupId>
        <artifactId>ipinfo-spring</artifactId>
        <version>0.5.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### Construction

Using this library is very simple. `IPinfoSpring` is exposed through a builder:

```java
    IPinfoSpring ipinfoSpring = new IPinfoSpring.Builder()
        // Set the IPinfo instance. By default we provide one, however you're
        // allowed to change this here. Also provide your IPinfo Access Token here.
        .setIPinfo(new IPinfo.Builder().setToken("IPINFO ACCESS TOKEN").build())
        // Set the InterceptorStrategy. By default we use
        // BotInterceptorStrategy.
        .interceptorStrategy(new BotInterceptorStrategy())
        // Set the IPStrategy. By default we use SimpleIPStrategy.
        .ipStrategy(new SimpleIPStrategy())
        // Set the AttributeStrategy. By default we use SessionAttributeStrategy.
        .attributeStrategy(new SessionAttributeStrategy())
        // Finally build it.
        .build();
```

### Adding to Interceptors

To use this as an interceptor in Spring, you simply need to expose your
configuration and add `IPinfoSpring` you obtained from the builder here:

````java
@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IPinfoSpring.Builder().build());
    }
}
````

### Accessing Value

There are two methods of getting the IPResponse that was injected into the
attributes:

1. Access it directly using the key defined in `IPinfoSpring`.
2. Access it using a reference to `attributeStrategy`.

The code below showcases the two different methods:

````java
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPinfoSpring;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class MainController {
    @Autowired
    private AttributeStrategy attributeStrategy;

    @RequestMapping("/foo")
    public String foo(HttpServletRequest request) {
        IPResponse ipResponse = attributeStrategy.getAttribute(request);

        if (ipResponse == null) {
            return "no ipresponse";
        }

        return ipResponse.toString();
    }

    @RequestMapping("/bar")
    public String bar(HttpServletRequest request) {
        IPResponse ipResponse = (IPResponse) request
                .getSession()
                .getAttribute(IPinfoSpring.ATTRIBUTE_KEY);

        if (ipResponse == null) {
            return "no ipresponse";
        }

        return ipResponse.toString();
    }
}
````

### `InterceptorStrategy`

The `InterceptorStrategy` allows the middleware to know when to actually run
the API calls to [ipinfo.io](https://ipinfo.io/).

- `BotInterceptorStrategy` (default)
  This does some very basic checks to see if the request is coming from a
  spider/crawler, and ignores them.

- `TrueInterceptorStrategy`
  This runs the API calls all the time.

### `IPStrategy`

The `IPStrategy` allows the middleware to know how to extract the IP address of
an incoming request.

Unfortunately, due to the topography of the web today, it's not as easy as
getting the IP address from the request. CDNs, reverse proxies, and countless
other technologies change the origin of a request that a web server can see.

- `SimpleIPStrategy` (default)
  This strategy simply looks at the IP of a request and uses that to extract
  more data using IPinfo.

- `XForwardedForIPStrategy`
  This strategy will extract the IP from the `X-Forwarded-For` header and if it's null
  it'll extract IP using `REMOTE_ADDR` of the client.

### `AttributeStrategy`

The `AttributeStrategy` allows the middleware to know where to store the
`IPResponse` from [ipinfo.io](https://ipinfo.io/).

- `SessionAttributeStrategy` (default)
  This strategy stores the IPResponse for the entire session. This would be
  much more efficient.

- `RequestAttributeStrategy`
  This strategy stores the IPResponse per request; this would lead to more API
  calls to [ipinfo.io](https://ipinfo.io/)

### Errors

Any exceptions such as `RateLimitedException` is passed through Spring's error
handling system.

### Lite API

The library gives the possibility to use the [Lite API](https://ipinfo.io/developers/lite-api) too, authentication with your token is still required.

The returned details are slightly different from the Core API.

To use the Lite API you must use the `IPinfoLiteSpring`, it works in the same way as the `IPinfoSpring` class.

```java
    IPinfoLiteSpring ipinfoSpring = new IPinfoLiteSpring.Builder()
        // Set the IPinfo instance. By default we provide one, however you're
        // allowed to change this here. Also provide your IPinfo Access Token here.
        .setIPinfo(new IPinfoLite.Builder().setToken("IPINFO ACCESS TOKEN").build())
        // Set the InterceptorStrategy. By default we use
        // BotInterceptorStrategy.
        .interceptorStrategy(new BotInterceptorStrategy())
        // Set the IPStrategy. By default we use SimpleIPStrategy.
        .ipStrategy(new SimpleIPStrategy())
        // Set the AttributeStrategy. By default we use SessionAttributeStrategy.
        .attributeStrategy(new SessionAttributeStrategy())
        // Finally build it.
        .build();
```

### Other Libraries

There are [official IPinfo client libraries](https://ipinfo.io/developers/libraries) available for many languages including PHP, Python, Go, Java, Ruby, and many popular frameworks such as Django, Rails, and Laravel. There are also many third-party libraries and integrations available for our API.

### About IPinfo

Founded in 2013, IPinfo prides itself on being the most reliable, accurate, and in-depth source of IP address data available anywhere. We process terabytes of data to produce our custom IP geolocation, company, carrier, VPN detection, hosted domains, and IP type data sets. Our API handles over 40 billion requests a month for businesses and developers.

[![image](https://avatars3.githubusercontent.com/u/15721521?s=128&u=7bb7dde5c4991335fb234e68a30971944abc6bf3&v=4)](https://ipinfo.io/)
