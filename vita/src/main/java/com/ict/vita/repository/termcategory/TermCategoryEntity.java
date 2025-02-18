package com.ict.vita.repository.termcategory;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Table(name = "APP_TERM_CATEGORY")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermCategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_TERM_CATEGORY_SEQ")
	@SequenceGenerator(name = "APP_TERM_CATEGORY_SEQ",sequenceName = "APP_TERM_CATEGORY_SEQ",initialValue = 1,allocationSize = 1)
	private long id; //PK
	
	//private long term_id;
	
	private String category; //카테고리명
	
	private String description; //용어에 대한 설명
	
	private int count; //해당 용어에 속하는 데이터 갯수
	
	private int parent; //부모 용어id(0이면 최상위 부모다)
	
}
