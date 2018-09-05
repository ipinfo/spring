# Spring-IPinfo: A spring middleware for the [IPinfo](https://ipinfo.io/) API.

[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![Travis](https://travis-ci.com/ipinfo/java-ipinfo.svg?branch=master&style=flat-square)](https://travis-ci.com/ipinfo/java-ipinfo)

Spring-IPinfo is a middleware for the IPinfo API, which provides up-to-date IP address data.

Contents:

- [Spring-IPinfo: A spring middleware for the IPinfo API.](#spring-ipinfo-a-spring-middleware-for-the-ipinfo-api)
    - [Features:](#features)
    - [Usage](#usage)
        - [Maven](#maven)
    - [Usage](#usage)
        - [Construction](#construction)
        - [Adding to Interceptors](#adding-to-interceptors)
        - [Accessing Value](#accessing-value)
    - [Structure](#structure)
        - [Strategies](#strategies)
            - [InterceptorStrategy](#interceptorstrategy)
                - [BotInterceptorStrategy](#botinterceptorstrategy)
                - [TrueInterceptorStrategy](#trueinterceptorstrategy)
                - [Default](#default)
            - [IPStrategy](#ipstrategy)
                - [SimpleIPStrategy](#simpleipstrategy)
                - [Default](#default)
            - [Attribute Strategy](#attribute-strategy)
                - [RequestAttributeStrategy](#requestattributestrategy)
                - [SessionAttributeStrategy](#sessionattributestrategy)
                - [Default](#default)
        - [Errors](#errors)

## Features:

- IP lookup for requests
- IP lookup for sessions
- The option to ignore bots

## Usage

### Maven
Repository:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
```

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>io.ipinfo</groupId>
        <artifactId>spring-ipinfo</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

----

## Usage

### Construction

Using this library is very simple. `IPInfoSpring` is exposed through a builder:

```java
    IPInfoSpringBuilder builder = IPInfoSpring.builder();
    
    // Set the IPInfo instance. By default we provide one, however you're allowed to change this here.
    builder.ipInfo(IPInfo.builder().build());

    // Set the InterceptorStrategy. By default we use BotInterceptorStrategy.
    builder.interceptorStrategy(new BotInterceptorStrategy());

    // Set the IPStrategy. By default we use SimpleIPStrategy.
    builder.ipStrategy(new SimpleIPStrategy());

    // Set the AttributeStrategy. By default we use SessionAttributeStrategy.
    builder.attributeStrategy(new SessionAttributeStrategy());

    // Finally build it into ipInfoSpring
    IPInfoSpring ipInfoSpring = builder.build();
```

### Adding to Interceptors
To use this as an interceptor in Spring, you simply need to expose your configuration and add `IPInfoSpring` you obtained from the builder here:

````java
@Configuration
public class ApplicationConfiguration implements WebMvcConfigurer {
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(IPInfoSpring.builder().build());
    }
}
````

### Accessing Value

There are two methods of getting the IPResponse that was injected into the attributes:

1. Access it directly using the key defined in `IPInfoSpring`.
2. Access it using a reference to `attributeStrategy`.

The code below showcases the two different methods:

````java
import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPInfoSpring;
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
        IPResponse ipResponse = (IPResponse) request.getSession().getAttribute(IPInfoSpring.ATTRIBUTE_KEY);

        if (ipResponse == null) {
            return "no ipresponse";
        }

        return ipResponse.toString();
    }
}
````

## Structure

At first glance, the way to use this code might look a little complicated. But rest assured that the code is designed in a way to be used within any sort of complexity of your code.

### Strategies

The code contains three main strategies.

1. `InterceptorStrategy`
2. `IPStrategy`
3. `AttributeStrategy`


#### InterceptorStrategy

The `InterceptorStrategy` allows the middleware to know when to actually run the API calls to [ipinfo.io](https://ipinfo.io/).

We provide two implementations of this:
 - `BotInterceptorStrategy`
 - `TrueInterceptorStrategy`
 
##### BotInterceptorStrategy

This does some very basic checks to see if the request is coming from a spider/crawler, and ignores them.

##### TrueInterceptorStrategy

This runs the API calls all the time.

##### Default

By default, we use the BotInterceptorStrategy.

#### IPStrategy

The `IPStrategy` allows the middleware to know how to extract the IP address of an incoming request.

Unfortunately, due to the topography of the web today, it's not as easy as getting the IP address from the request. CDNs, reverse proxies, and countless other technologies change the origin of a request that a web server can see.

We provide a very simple implementation of IPStrategy called `SimpleIPStrategy`.
 
##### SimpleIPStrategy 
This strategy simply looks at the IP of a request and uses that to extract more data using IPInfo.

##### Default

By default we use `SimpleIPStrategy`. 

#### Attribute Strategy

The `AttributeStrategy` allows the middleware to know where to store the `IPResponse` from [ipinfo.io](https://ipinfo.io/).

We provide two implementations of this:
 - `RequestAttributeStrategy`
 - `SessionAttributeStrategy`

##### RequestAttributeStrategy  
This strategy stores the IPResponse per request, this would lead the more API calls to [ipinfo.io](https://ipinfo.io/)

##### SessionAttributeStrategy
This strategy stores the IPResponseor the entire session. This would be much more efficient.

##### Default
By default we use `SessionAttributeStrategy` and we recommend you stick to that.

As with all the strategies, you can implement and pass in your own custom ones to `IPInfoSpring`.

### Errors

Any exceptions such as `RateLimitedException` is passed through Spring's error handling system.