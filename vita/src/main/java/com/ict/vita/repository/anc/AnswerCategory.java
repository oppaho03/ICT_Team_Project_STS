package com.ict.vita.repository.anc;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//[APP_ANC 테이블의 복합키]
@Embeddable
public class AnswerCategory implements Serializable{
	private Long term_category_id; //카테고리 id
	private Long answer_id; //답변 id
}
