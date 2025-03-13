package com.ict.vita.controller.dhlogin;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DHLoginController {
	
    @PostMapping("/login.do")
    public String loginPage(@RequestParam Map<String, String> login, Model model) {
    	String username = login.get("username");
    	String password = login.get("password");
    	
    	if("kim".equals(username) && "kim1234".equals(password)) {
    		model.addAttribute("username",username);
    		return "DH/login";
    	}
    	else {
    		model.addAttribute("error","아이디 또는 비밀번호 불일치");
    		return "DH/dhpage";
    	}
    	
    }

}
