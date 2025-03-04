package com.ict.vita.repository.chatqna;

import com.ict.vita.repository.chatanswer.ChatAnswerEntity;
import com.ict.vita.repository.chatquestion.ChatQuestionEntity;
import com.ict.vita.repository.chatsession.ChatSessionEntity;
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

@Table(name = "APP_CHAT_QNA")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[큐앤에이(관계)]
public class ChatQnaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_CHAT_QNA_SEQ")
	@SequenceGenerator(name = "APP_CHAT_QNA_SEQ",sequenceName = "APP_CHAT_QNA_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "s_id",nullable = false) //테이블에서 FK명
	@NotNull
	private ChatSessionEntity chatSessionEntity; //세션
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "q_id",nullable = false) //테이블에서 FK명
	@NotNull
	private ChatQuestionEntity chatQuestionEntity; //질문
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "a_id",nullable = false) //테이블에서 FK명
	@NotNull
	private ChatAnswerEntity chatAnswerEntity; //답변
	
	@NotNull
	@Column(columnDefinition = "NUMBER(1,0)")
	private long is_matched; //매칭여부(1:매칭됨,0:매칭X)
}
