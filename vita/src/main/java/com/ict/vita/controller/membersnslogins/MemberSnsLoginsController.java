package com.ict.vita.controller.membersnslogins;

import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.membersnslogins.MemberSnsLoginsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberSnsLoginsController {
	//서비스 주입
	private final MemberSnsLoginsService memberSnsLoginsService;
	
}
