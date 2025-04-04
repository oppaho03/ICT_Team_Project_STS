package com.ict.vita.service.member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.member.MemberRepository;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
	//리포지토리 주입
	private final MemberRepository memberRepository;
	
	/**
	 * [모든 회원 조회]
	 * @return List<MemberDto>
	 */
	@Transactional(readOnly = true)
	public List<MemberDto> getAllMembers(){
		return memberRepository.findAll().stream().map(entity -> MemberDto.toDto(entity)).collect(Collectors.toList());
	}
	
	/**
	 * [id(PK)로 회원 찾기]
	 * @param memberDto 찾을 회원 DTO객체
	 * @return MemberDto 찾은 회원 DTO객체(없으면 null)
	 */
	@Transactional(readOnly = true)
	public MemberDto findMemberById(Long id) {
		//회원을 찾으면 찾은 회원 Entity객체를,
		// 일치하는 회원이 존재하지 않으면 null을 반환
		MemberEntity findedMember = memberRepository.findById(id).orElse(null);
		if(findedMember != null) {
			return MemberDto.toDto(findedMember);
		}
		return null;
	}
	
	/**
	 * [토큰값으로 회원 찾기]
	 * @param token 찾을 토큰값
	 * @return MemberDto 찾은 회원 DTO객체(없으면 null)
	 */
	@Transactional(readOnly = true)
	public MemberDto findMemberByToken(String token) {
		//회원을 찾으면 찾은 회원 Entity객체를,
		// 일치하는 회원이 존재하지 않으면 null을 반환
		MemberEntity findedMember = memberRepository.findByToken(Commons.parseHTTPHeaderToken(token)).orElse(null);
		if(findedMember != null) {
			return MemberDto.toDto(findedMember);
		}
		return null;
	} 
	
	/**
	 * [이메일로 회원 찾기]
	 * @param email 이메일
	 * @return MemberDto 찾은 회원 DTO객체(없으면 null)
	 */
	@Transactional(readOnly = true)
	public MemberDto findMemberByEmail(String email) {
		//회원을 찾으면 찾은 회원 Entity객체를,
		// 일치하는 회원이 존재하지 않으면 null을 반환
		MemberEntity findedMember = memberRepository.findByEmail(email).orElse(null);
		if(findedMember != null) {
			return MemberDto.toDto(findedMember);
		}
		return null;
	}
	
	/**
	 * [이메일 중복 여부 판단]
	 * @param email 입력된 이메일 주소
	 * @return 가입된 이메일이 존재하는지 여부
	 */
	@Transactional(readOnly = true)
	public boolean isExistsEmail(String email) {
		return memberRepository.existsByEmail(email);
	}

	/**
	 * [전화번호 중복 여부 판단]
	 * @param contact 입력된 전화번호
	 * @return 가입된 전화번호가 존재하는지 여부
	 */
	@Transactional(readOnly = true)
	public boolean isExistsContact(String contact) {
		return memberRepository.existsByContact(contact);
	}
	
	/**
	 * [임시 회원가입]
	 * @param tempJoinDto 임시 회원가입용 DTO
	 * @return MemberDto 임시로 가입된 회원 DTO
	 */
	public MemberDto tempJoin(MemberTempJoinDto tempJoinDto) {
		//임시 회원가입 객체 생성
		MemberJoinDto joinDto = MemberJoinDto.builder()
									.email(tempJoinDto.getEmail())
									.password(tempJoinDto.getPassword()) 
									.role(tempJoinDto.getRole())
									.name(tempJoinDto.getName())
									.nickname(tempJoinDto.getNickname())
									.birth(tempJoinDto.getBirth())
									.gender(tempJoinDto.getGender())
									.contact(tempJoinDto.getContact())
									.address(tempJoinDto.getAddress())
									.token(tempJoinDto.getToken())
									.created_at(LocalDateTime.now())
									.updated_at(LocalDateTime.now())
									.status(9) //임시 가입 상태(9)
									.isEmailAuth(0) //이메일 인증 안됐다(0) 처리
									.build(); 
		
		String nickname = joinDto.getNickname();

		//닉네임 미입력시
		if(Commons.isNull(nickname)) {
			nickname = Commons.getAutoNickname(tempJoinDto.getEmail(), nickname);
		}
	
		//회원 저장
		MemberEntity entity = memberRepository.save(MemberEntity.builder()
								.email(joinDto.getEmail())
								.password(joinDto.getPassword()) 
								.role(joinDto.getRole())
								.name(joinDto.getName())
								.nickname(nickname)
								.birth(joinDto.getBirth())
								.gender(joinDto.getGender())
								.contact(joinDto.getContact())
								.address(joinDto.getAddress())
								.created_at(joinDto.getCreated_at())
								.updated_at(joinDto.getUpdated_at())
								.status(9) //임시 회원가입(9)
								.build());
		
		return entity != null ? MemberDto.toDto(entity) : null;
	}
	
	/**
	 * [회원가입 처리] - 최종 회원가입 처리
	 * @param joinDto 임시 저장된 회원 DTO 객체
	 * @return
	 */
	public MemberDto join(MemberDto joinDto) {	
		
		MemberEntity entity = null;
		
		//닉네임 설정
		String nickname = joinDto.getNickname();
		if(Commons.isNull(joinDto.getNickname())) nickname = Commons.getAutoNickname(joinDto.getEmail(), joinDto.getNickname());
		
		//회원 저장		
		try {
			
			entity = memberRepository.save(MemberEntity.builder()
							.id(joinDto.getId()) //임시회원가입된 회원 정보 수정시 PK값 지정 필요
							.email(joinDto.getEmail())
							.password(EncryptAES256.encrypt(joinDto.getPassword())) //비밀번호 암호화
							.role(joinDto.getRole())
							.name(joinDto.getName())
							.nickname(nickname)
							.birth(joinDto.getBirth())
							.gender(joinDto.getGender())
							.contact(joinDto.getContact())
							.address(joinDto.getAddress())
							.token(joinDto.getToken())
							.created_at(LocalDateTime.now())
							.updated_at(LocalDateTime.now())
							.status(1) //회원가입 처리(1)
							.build());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		return MemberDto.toDto(entity);
		
	}////
	
	/**
	 * [direct 로그인 시]
	 * 아이디 및 비밀번호 검증
	 * @param loginDto
	 * @return
	 */
	@Transactional(readOnly = true)
	public MemberDto validateLogin(MemberLoginDto loginDto) {
		//리포지토리 호출해서 이메일과 비밀번호가 일치하면 해당하는 DTO를,
		// 일치하는 회원이 존재하지 않으면 null을 반환
		MemberEntity findedMember = memberRepository.findByEmailIsAndPasswordIs(loginDto.getEmail(), EncryptAES256.encrypt(loginDto.getPassword()) ).orElse(null);
		if(findedMember != null && findedMember.getStatus() == 1)
			return MemberDto.toDto(findedMember);
		return null;
	}
	
	/**
	 * [회원정보 수정]
	 * @param memberDto 수정할 회원 DTO 객체
	 * @return 수정된 회원 DTO 객체
	 */
	public MemberDto updateMember(MemberDto memberDto) {
		MemberEntity memberEntity = memberRepository.save(memberDto.toEntity());
		return MemberDto.toDto(memberEntity);
	}

	/**
	 * [status에 해당하는 회원 조회]
	 * @param status 회원 status값
	 * @return List<MemberDto> 
	 */
	@Transactional(readOnly = true)
	public List<MemberDto> findMemberByStatus(Long status) {
		return memberRepository.findAllByStatus(status)
			.stream()
			.map(entity -> MemberDto.toDto(entity))
			.collect(Collectors.toList());
	}

	/**
	 * [role에 해당하는 회원 조회]
	 * @param role 회원 역할
	 * @return List<MemberDto> 
	 */
	@Transactional(readOnly = true)
	public List<MemberDto> findMemberByRole(String role) {
		return memberRepository.findAllByRole(role)
			.stream()
			.map(entity -> MemberDto.toDto(entity))
			.collect(Collectors.toList());
	}

	/**
	 * [회원 탈퇴]
	 * @param mid 탈퇴할 회원 id
	 * @return MemberDto 탈퇴한 회원 객체
	 */
	public MemberDto withdrawMember(Long mid) {
		MemberDto findedMember = findMemberById(mid);
		//<회원의 status를 0 으로 수정>
		findedMember.setStatus(0); //탈퇴 처리
		MemberDto withdrawnMember = updateMember(findedMember);
		
		return withdrawnMember;
	}
	
}
