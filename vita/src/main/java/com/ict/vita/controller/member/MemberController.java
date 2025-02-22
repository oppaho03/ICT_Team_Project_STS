package com.ict.vita.controller.member;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberController {
	//서비스 주입
	private final MemberService memberService;
	
	
}
