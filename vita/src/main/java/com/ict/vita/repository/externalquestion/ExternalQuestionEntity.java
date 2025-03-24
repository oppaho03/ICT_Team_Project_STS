package com.ict.vita.repository.externalquestion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_EXTERNAL_QUESTION")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[외부 질문 엔터티 - 통계용]
public class ExternalQuestionEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_EXTERNAL_QUESTION_SEQ")
	@SequenceGenerator(name = "APP_EXTERNAL_QUESTION_SEQ",sequenceName = "APP_EXTERNAL_QUESTION_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@Column(name = "file_name",columnDefinition = "VARCHAR2(20)")
	private String fileName; //파일명
	
	@Column(columnDefinition = "CHAR(1)")
	private char gender; //성별
	
	@Column(columnDefinition = "NVARCHAR2(20 CHAR)")
	private String age; //나이
	
	@Column(columnDefinition = "NVARCHAR2(100 CHAR)")
	private String occupation; //직업
	
	@Column(name = "disease_category",columnDefinition = "NVARCHAR2(200 CHAR)")
	private String diseaseCategory; //질병 카테고리명
	
	@Column(name = "disease_name_kor",columnDefinition = "NVARCHAR2(200 CHAR)")
	private String diseaseNameKor; //한글 질병명
	
	@Column(name = "disease_name_eng",columnDefinition = "NVARCHAR2(200 CHAR)")
	private String diseaseNameEng; //영어 질병명
	
	@Lob
	@Column(columnDefinition = "CLOB DEFAULT EMPTY_CLOB()")
	private String question; //질문 내용
}
