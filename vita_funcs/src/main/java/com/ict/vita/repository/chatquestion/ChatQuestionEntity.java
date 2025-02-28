package com.ict.vita.repository.chatquestion;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_CHAT_QUESTION")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[질문]
public class ChatQuestionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_CHAT_QUESTION_SEQ")
	@SequenceGenerator(name = "APP_CHAT_QUESTION_SEQ",sequenceName = "APP_CHAT_QUESTION_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK  
	
	@Lob
	@NotNull
	private String content; //내용
	
	@Column(columnDefinition = "TIMESTAMP")
	@ColumnDefault("SYSDATE")
	@CreationTimestamp //생성될때 현재 날짜로
	@NotNull
	private LocalDateTime created_at; //생성일
}
