package com.ict.vita.repository.terms;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "APP_TERMS")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[용어]
public class TermsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_TERMS_SEQ")
	@SequenceGenerator(name = "APP_TERMS_SEQ",sequenceName = "APP_TERMS_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK  
	
	@NotNull
	@Column(columnDefinition = "NVARCHAR2(200)") //NVARCHAR2로 명시
	private String name = ""; //용어 이름
	
	@NotNull
	@Column(columnDefinition = "VARCHAR2(200)") //VARCHAR2로 명시
	private String slug = ""; //용어 슬러그
	
	@NotNull
	@ColumnDefault(value = "0") //@ColumnDefault: DB테이블에 영향
	@Column(columnDefinition = "NUMBER(20,0)")
	private long group_number = 0; //용어 그룹번호
	
}
