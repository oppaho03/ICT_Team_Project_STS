package com.ict.vita.service.terms;

import java.time.LocalDateTime;
import com.ict.vita.repository.terms.TermsEntity;
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
public class TermsDto {
	private long id; //PK 
	private String name; //용어 이름
	private String slug; //용어 슬러그
	private int group_number; //용어 그룹번호
	
	//[Terms엔터티를 DTO로 변환하는 메서드]
	public TermsEntity toEntity() {
		return TermsEntity.builder()
				.id(id)
				.name(name)
				.slug(slug)
				.group_number(group_number)
				.build();
	}
	
	//[TermsDTO를 Terms엔터티로 변환하는 메서드]
	public static TermsDto toDto(TermsEntity terms) {
		return TermsDto.builder()
				.id(terms.getId())
				.name(terms.getName())
				.slug(terms.getSlug())
				.group_number(terms.getGroup_number())
				.build();
	}
}
