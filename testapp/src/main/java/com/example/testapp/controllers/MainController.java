package com.example.testapp.controllers;

import io.ipinfo.api.model.IPResponse;
import io.ipinfo.spring.IPinfoSpring;
import io.ipinfo.spring.strategies.attribute.AttributeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import  jakarta.servlet.http.HttpServletRequest;

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
        IPResponse ipResponse = (IPResponse) request.getSession().getAttribute(IPinfoSpring.ATTRIBUTE_KEY);
        if (ipResponse == null) {
            return "no ipresponse";
        }

        return ipResponse.toString();
    }
}
