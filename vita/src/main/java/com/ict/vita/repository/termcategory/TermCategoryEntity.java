package com.ict.vita.repository.termcategory;

import org.hibernate.annotations.ColumnDefault;

import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_TERM_CATEGORY")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[카테고리(텍소노미)]
public class TermCategoryEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_TERM_CATEGORY_SEQ")
	@SequenceGenerator(name = "APP_TERM_CATEGORY_SEQ",sequenceName = "APP_TERM_CATEGORY_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@OneToOne
	@JoinColumn(name = "term_id",nullable = false) //테이블에서 FK명
	@NotNull
	//@Column(columnDefinition = "NUMBER(20,0)")
	private TermsEntity termsEntity; //용어
	
	@NotNull
	@Column(unique = true,length = 20) //String 타입은 기본적으로 VARCHAR2로 매핑
	private String category; //카테고리명
	
	@Lob //데이터베이스의 BLOB, CLOB 타입과 매핑
	private String description; //용어에 대한 설명
	
	@ColumnDefault(value = "0")
	@Column(columnDefinition = "NUMBER(20,0)") //NUMBER(20,0)을 명확하게 설정하려면 @Column의 columnDefinition에 명시해야 함
	private long count; //해당 용어에 속하는 데이터 갯수
	
	@ColumnDefault(value = "0")
	@Column(columnDefinition = "NUMBER(20,0)") //NUMBER(20,0)을 명확하게 설정하려면 @Column의 columnDefinition에 명시해야 함
	private long parent = 0; //부모 용어id(0이면 최상위 부모다)
	
}
