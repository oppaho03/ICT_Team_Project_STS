package com.ict.vita.repository.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_MEMBER")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[회원]
public class MemberEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_MEMBER_SEQ")
	@SequenceGenerator(name = "APP_MEMBER_SEQ",sequenceName = "APP_MEMBER_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@Column(columnDefinition = "VARCHAR2(255 CHAR)",unique = true)
	@NotNull
	private String email; //이메일
	
	@Column(columnDefinition = "VARCHAR2(255 CHAR)")
	@NotNull
	private String password; //비밀번호
	
	@Column(columnDefinition = "VARCHAR2(20 CHAR)")
	@NotNull
	private String role; //역할
	
	private String name; //이름
	
	private String nickname; //닉네임
	
	private LocalDate birth; //생년월일
	
	private char gender; //성별
	
	private String contact; //전화번호
	
	private String address; //주소
	
	private String token; //활성화 토큰
	
	private LocalDateTime created_at; //가입일
	
	private LocalDateTime updated_at; //수정일
	
	private long status; //상태(가입/탈퇴)
}
