package com.ict.vita.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index"; // index.html을 반환
    }

    @GetMapping("/yw")
    public String ywPage() {
        return "YW/yw"; // yw.html로 이동
    }

    @GetMapping("/dhpage")
    public String dhPage() {
        return "DH/dhpage"; // dh.html로 이동
    }


}