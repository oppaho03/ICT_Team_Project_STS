package com.ict.vita.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/dh")
    public String dhPage() {
        return "DH/dh"; // dh.html로 이동
    }
}