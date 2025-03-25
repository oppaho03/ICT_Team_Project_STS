package com.ict.vita.repository.chatsession;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_CHAT_SESSION")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[대화 세션]
public class ChatSessionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_CHAT_SESSION_SEQ")
	@SequenceGenerator(name = "APP_CHAT_SESSION_SEQ",sequenceName = "APP_CHAT_SESSION_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id",nullable = false) //테이블에서 FK명
	@NotNull
	private MemberEntity memberEntity; //회원
	
	@NotNull
	@Column(name = "created_at",columnDefinition = "TIMESTAMP")
	@ColumnDefault("SYSDATE")
	@CreationTimestamp
	private LocalDateTime createdAt; //생성일
	
	@NotNull
	@Column(name = "updated_at",columnDefinition = "TIMESTAMP")
	private LocalDateTime updatedAt; //수정일
	
	@NotNull
	@Column(columnDefinition = "NUMBER(1,0)")
	private long status; //상태(공개(0) / 비공개(1)(디폴트))
	
	@NotNull
	@Column(columnDefinition = "NUMBER(20,0)")
	@ColumnDefault("0")
	private long count = 0; //카운트(공개상태일때 외부인이 조회한 횟수)
}
