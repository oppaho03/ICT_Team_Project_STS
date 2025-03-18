package com.ict.vita.repository.keywordcounting;

import org.hibernate.annotations.ColumnDefault;

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

@Table(name = "APP_KEYWORD_COUNTING")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[키워드 카운팅]
public class KeywordCountingEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_KEYWORD_COUNTING_SEQ")
	@SequenceGenerator(name = "APP_KEYWORD_COUNTING_SEQ",sequenceName = "APP_KEYWORD_COUNTING_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK  
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "term_id",nullable = false) //테이블에서 FK명
	@NotNull
	private TermsEntity termsEntity; //용어
	
	@NotNull
	@Column(columnDefinition = "NUMBER(10,0)")
	@ColumnDefault(value = "1")
	private long count = 1; //카운팅 수
	
	@NotNull
//	@ColumnDefault(value = "19700101")
	@Column( name="searched_at", columnDefinition = "VARCHAR2(10) DEFAULT '19700101'") //VARCHAR2로 명시
	private String searchedAt = "19700101"; //검색날짜(월간/일간)
	
}
