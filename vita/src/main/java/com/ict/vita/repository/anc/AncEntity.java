package com.ict.vita.repository.anc;

import com.ict.vita.repository.chatanswer.ChatAnswerEntity;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_ANC")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[답변카테고리(관계)]
public class AncEntity {
	 @EmbeddedId //복합키 구현
	 private AnswerCategory id; //PK(FK2개로 이루어진 복합키)
	 
	 @MapsId("answer_id") //해당 FK가 복합키(@EmbeddedId)의 어떤 변수인지를 설정
	 @JoinColumn(name = "answer_id") //DB에서 외래키명 설정
	 @ManyToOne(fetch = FetchType.LAZY)
	 private ChatAnswerEntity chatAnswerEntity;
	 
	 @MapsId("term_category_id") //해당 FK가 복합키(@EmbeddedId)의 어떤 변수인지를 설정
	 @JoinColumn(name = "term_category_id") //DB에서 외래키명 설정
	 @ManyToOne(fetch = FetchType.LAZY)
	 private TermCategoryEntity termCategoryEntity;
}
