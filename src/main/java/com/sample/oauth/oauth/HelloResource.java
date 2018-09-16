package com.sample.oauth.oauth;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloResource {


    @GetMapping(path = "hello")
    public String hello(String name, HttpServletRequest request) {
        return "hello "+ name + "!";
    }

}
