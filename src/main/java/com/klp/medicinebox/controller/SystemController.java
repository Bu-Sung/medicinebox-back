package com.klp.medicinebox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SystemController {

    @GetMapping("/")
    public String pageMain(){
        return "index";
    }
}
