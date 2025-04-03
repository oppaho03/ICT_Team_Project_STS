package com.ict.vita.repository.membersnslogins;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_MEMBER_SNS_LOGINS")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[회원 SNS 로그인]
public class MemberSnsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_MEMBER_SNS_LOGINS_SEQ")
	@SequenceGenerator(name = "APP_MEMBER_SNS_LOGINS_SEQ",sequenceName = "APP_MEMBER_SNS_LOGINS_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id",nullable = false) //테이블에서 FK명
	@NotNull
	private MemberEntity memberEntity; //회원
	
	@Column(columnDefinition = "VARCHAR2(100)")
	@NotNull
	@ColumnDefault("")
	private String login_id = ""; //로그인ID 또는 이메일
	
	@Column(columnDefinition = "VARCHAR2(20)")
	@NotNull
	private String provider; //인증서버
	
	@Column(columnDefinition = "VARCHAR2(255)",unique = true)
	@NotNull
	private String provider_id; //인증서버 발급ID
	
	@Lob
	private String access_token; //인증토큰
	
	@Lob
	private String refresh_token; //인증토큰 갱신을 위한 토큰
	
	@Column(columnDefinition = "NUMBER(1)")
	@NotNull
	@ColumnDefault("1")
	private long status = 1; //상태(계정과 연결/해제)(1: 계정과 연결)
	
	@Column(columnDefinition = "TIMESTAMP")
	@NotNull
	private LocalDateTime login_modified_at; //로그인 일시
	
	@Column(columnDefinition = "TIMESTAMP")
	@ColumnDefault("SYSDATE")
	@CreationTimestamp //생성될때 현재 날짜로
	@NotNull
	private LocalDateTime login_created_at; //생성일시(최초)
}
