package com.ict.vita.controller.membersnslogins;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsDto;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsRequestDto;
import com.ict.vita.service.membersnslogins.MemberSnsLoginsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class MemberSnsLoginsController {
	//서비스 주입
	private final MemberSnsLoginsService memberSnsLoginsService;
	private final MemberService memberService;
	
	/**
	 * [SNS 로그인]
	 * @param infos SNS로그인요청 정보(email, token 들어있음)
	 * @return ResponseEntity
	 */
	@PostMapping("/api/sns_login")
	public ResponseEntity<?> snsLogin(@RequestBody MemberSnsLoginsRequestDto snsReqDto){
		
		System.out.println(String.format("[sns]이메일:%s, 인증토큰:%s", snsReqDto.getEmail(), snsReqDto.getToken()));
		
		//<sns회원 테이블에서 조회>
		MemberSnsLoginsDto snsDto = memberSnsLoginsService.getSnsMemberByEmail(snsReqDto.getEmail().trim());
		//<회원 테이블에서 이메일 조회>
		MemberDto findedMember = memberService.findMemberByEmail(snsReqDto.getEmail().trim());
		
		//[sns회원 존재시]
		if(snsDto != null) {
			//회원 테이블에 존재
			if(findedMember.getStatus() != 1) { 
				findedMember.setStatus(1);
				memberService.updateMember(findedMember);
			}
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		}
		
		//[sns회원 미존재시]
		//회원 테이블에 없을 때
		if(findedMember == null) {
			//회원 테이블에 저장
			findedMember = MemberDto.builder()
									.email(snsReqDto.getEmail().trim())
									.password(EncryptAES256.encrypt(snsReqDto.getToken()) )
									.nickname( snsReqDto.getEmail().trim().split("@")[0] )
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(1)
									.build();
			findedMember = memberService.join(findedMember);
			//sns회원 테이블에 저장
			snsDto = MemberSnsLoginsDto.builder()
									.memberDto(findedMember)
									.login_id( snsReqDto.getEmail().trim() )
									.access_token(snsReqDto.getToken().trim())
									.status(1)
									.login_created_at(LocalDateTime.now())
									.login_modified_at(LocalDateTime.now())
									.provider(Commons.TEMPORARY)
									//.provider_id(Commons.TEMPORARY) //유니크한 값
									.build();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		}
		
		//회원 테이블에 있을 때
		if(findedMember.getStatus() != 1) {
			findedMember.setStatus(1);
			findedMember.setPassword(EncryptAES256.encrypt(snsReqDto.getToken()));
			findedMember = memberService.updateMember(findedMember);
		}
		
		//sns회원 테이블에 저장
		snsDto = MemberSnsLoginsDto.builder()
								.memberDto(findedMember)
								.login_id( snsReqDto.getEmail().trim() )
								.access_token(snsReqDto.getToken().trim())
								.status(1)
								.login_created_at(LocalDateTime.now())
								.login_modified_at(LocalDateTime.now())
								.provider(Commons.TEMPORARY)
								//.provider_id(Commons.TEMPORARY) //유니크한 값
								.build();
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(snsDto));
		
		
		/*
		//<회원 테이블에서 이메일 존재여부 확인>
		//회원 존재시
		if(memberService.isExistsEmail(snsReqDto.getEmail().trim())) {

			MemberDto findedMember = memberService.findMemberByEmail(snsReqDto.getEmail().trim()); 
			
			//탈퇴(0)한 회원 또는 임시가입(9)된 회원인 경우
			if(findedMember.getStatus() == 0 || findedMember.getStatus() == 9) {
				findedMember.setStatus(1);
				try {
					findedMember.setPassword( EncryptAES256.encrypt(snsReqDto.getToken().trim()) );
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("SNS 로그인 처리 중 비밀번호 암호화 실패1");
				}
			}
			
			//회원 sns로그인 테이블에서 조회
			MemberSnsLoginsDto findedSnsMember = memberSnsLoginsService.getSnsMemberByEmail(snsReqDto.getEmail().trim());
			
			//sns회원 테이블에 존재시
			if(findedSnsMember != null) {
				findedSnsMember.setAccess_token(snsReqDto.getToken().trim());
				findedSnsMember.setLogin_modified_at(LocalDateTime.now());
			}
			
			//sns회원 테이블에 미존재시
			else {
				
				findedSnsMember = MemberSnsLoginsDto.builder()
											.memberDto(findedMember)
											.login_id(snsReqDto.getEmail())
											.provider(Commons.TEMPORARY)
											//.provider_id(Commons.TEMPORARY) //유니크한 값
											.access_token(snsReqDto.getToken())
											.refresh_token(null)
											.status(1)
											.login_modified_at(LocalDateTime.now())
											.login_created_at(LocalDateTime.now())
											.build();
				
				//sns회원 테이블에 저장
				findedSnsMember = memberSnsLoginsService.save(findedSnsMember);
			}
			
			return findedSnsMember != null ? ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(findedSnsMember))
					: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("sns회원 저장 실패"));
			
		}
		
		//회원 미 존재시
		MemberDto memberDto = null;
		try {
			memberDto = MemberDto.builder()
									.email(snsReqDto.getEmail().trim())
									.password(EncryptAES256.encrypt(snsReqDto.getToken().trim()))
									.nickname( snsReqDto.getEmail().trim().split("@")[0] )
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(1)
									.build();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("SNS 로그인 처리 중 비밀번호 암호화 실패2");
		}
		//회원 테이블에 저장
		MemberDto savedMember = memberService.join(memberDto);
		//sns회원 테이블에 저장
		MemberSnsLoginsDto snsDto = MemberSnsLoginsDto.builder()
									.memberDto(savedMember)
									.login_id(savedMember.getEmail())
									.access_token(snsReqDto.getToken().trim())
									.status(1)
									.login_created_at(LocalDateTime.now())
									.login_modified_at(LocalDateTime.now())
									.provider(Commons.TEMPORARY)
									//.provider_id(Commons.TEMPORARY) //유니크한 값
									.build();
		MemberSnsLoginsDto savedSnsMember = memberSnsLoginsService.save(snsDto);
		
		return savedSnsMember != null ? ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(savedSnsMember))
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("sns회원 저장 실패"));
	} */
	}
	
}
