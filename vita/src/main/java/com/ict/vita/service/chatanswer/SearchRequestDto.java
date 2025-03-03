package com.ict.vita.service.chatanswer;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//[키워드로 검색시 서버에서 키워드를 받을 DTO 객체]
/* 
	 { "keywords" : ["감염", "여부", "확인", ....] }
	 와 같은 json형식으로 데이터 받음
*/
public class SearchRequestDto {
	private List<String> keywords;
}
