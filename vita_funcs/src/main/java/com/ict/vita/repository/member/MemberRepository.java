package com.ict.vita.repository.member;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	//[메서드 네임 규칙에 따른 추상 메서드 추가]
	Optional<MemberEntity> findById(Long id); //id(PK)로 회원 조회
	boolean existsByEmail(String email); //중복 이메일 검증용
	boolean existsByContact(String contact); //중복 전화번호 검증용
	
}
