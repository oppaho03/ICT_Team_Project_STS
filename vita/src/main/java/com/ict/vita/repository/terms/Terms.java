package com.ict.vita.repository.terms;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Table(name = "APP_TERMS") 
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[용어]
public class Terms {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_TERMS_SEQ")
	@SequenceGenerator(name = "APP_TERMS_SEQ",sequenceName = "APP_TERMS_SEQ",initialValue = 1,allocationSize = 1)
	private int id; //PK  //PK타입을 int? long?
	
	@NotBlank
	private String name = ""; //용어 이름
	
	@NotBlank
	private String slug = ""; //용어 슬러그
	
	@NotBlank
	@ColumnDefault(value = "0") //@ColumnDefault: DB테이블에 영향
	private int group_number = 0; //용어 그룹번호
	
}
