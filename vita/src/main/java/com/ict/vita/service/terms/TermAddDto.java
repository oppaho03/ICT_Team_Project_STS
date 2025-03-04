package com.ict.vita.service.terms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[용어 DTO]
public class TermAddDto {
	@Schema(description = "표시될 용어 명칭", example = "카테고리")
	@NotBlank(message = "이름을 입력하세요.")
	private String name; //용어 이름
	@Schema(description = "슬러그", example = "category")	
	private String slug = ""; //용어 슬러그
	@Schema(description = "그룹 번호", example = "0")	
	private long group_number = 0; //용어 그룹번호
	
	@Schema(description = "카테고리명(Taxonomy)", example = "category")	
	private String category="category"; //카테고리명
	@Schema(description = "용어에 대한 부가 설명", example = "")	
	private String description = ""; //용어에 대한 설명
	@Schema(description = "하위 컨텐츠의 총 수", example = "0")	
	private long count = 0; //해당 용어에 속하는 데이터 갯수
	@Schema(description = "부코 용어", example = "0")
	private long parent = 0; //부모 용어id(0이면 최상위 부모다)
	
}
