package com.quantech.controller;

import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController{
    @Override
    public String getErrorPath() {
        return "redirect:/error";
    }
    @RequestMapping(value = "/error")
    public String error() {
        return "Error/error";
    }
}
