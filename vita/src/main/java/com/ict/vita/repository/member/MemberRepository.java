package com.ict.vita.repository.member;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	//[메서드 네임 규칙에 따른 추상 메서드 추가]
	boolean existsByEmail(String email); //중복 이메일 검증용
	boolean existsByContact(String contact); //중복 전화번호 검증용
	Optional<MemberEntity> findByEmailIsAndPasswordIs(String email,String password); //로그인 검증용
	Optional<MemberEntity> findByToken(String token); //토큰값으로 회원 조회용
	Optional<MemberEntity> findByEmail(String email); //이메일로 회원 조회용
	List<MemberEntity> findAllByStatus(Long status); //status로 회원 조회용
	List<MemberEntity> findAllByRole(String role); //role로 회원 조회용
}
