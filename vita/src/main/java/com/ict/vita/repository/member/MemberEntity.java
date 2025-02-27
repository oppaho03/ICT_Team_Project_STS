package com.ict.vita.repository.member;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
	
	@Column(columnDefinition = "VARCHAR2(255)",unique = true)
	@NotNull
	@Email(message = "올바른 이메일 주소를 입력해주세요.") //이메일 형식에 맞는지 검사
	private String email; //이메일
	
	@Column(columnDefinition = "VARCHAR2(255)")
	@NotNull
	private String password; //비밀번호
	
	@Column(columnDefinition = "VARCHAR2(20)")
	@ColumnDefault("USER")
	@NotNull
	private String role = "USER"; //역할
	
	@Column(columnDefinition = "NVARCHAR2(100)")
	private String name; //이름
	
	@Column(columnDefinition = "NVARCHAR2(255)")
	@NotNull
	private String nickname; //닉네임
	
	@Column(columnDefinition = "DATE")
	private LocalDate birth; //생년월일
	
	@Column(columnDefinition = "CHAR(1)")
	private char gender; //성별
	
	@Column(columnDefinition = "VARCHAR2(20)")
	private String contact; //전화번호
	
	@Column(columnDefinition = "NVARCHAR2(500)")
	private String address; //주소
	
	@Column(columnDefinition = "VARCHAR2(255)")
	@ColumnDefault("")
	private String token = ""; //활성화 토큰
	
	@Column(columnDefinition = "TIMESTAMP")
	@ColumnDefault("SYSDATE")
	@CreationTimestamp //생성될때 현재 날짜로
	@NotNull
	private LocalDateTime created_at; //가입일
	
	@Column(columnDefinition = "TIMESTAMP")
	@NotNull
	private LocalDateTime updated_at; //수정일
	
	@NotNull
	@Column(columnDefinition = "NUMBER(1,0)")
	private long status; //상태(가입/탈퇴)
}
