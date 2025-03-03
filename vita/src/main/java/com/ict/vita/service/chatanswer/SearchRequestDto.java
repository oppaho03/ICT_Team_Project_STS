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
	 { "data" : [
	         {"keyword" : "검사"},
	         {"keyword" : "항원"},
	             .........
	   ]   
	 } 
	 와 같은 json형식으로 데이터 받음
*/
public class SearchRequestDto {
	private List<Keyword> data;
	
	//내부 클래스
	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Keyword{ //내부클래스 static으로 안 하면 에러남(static 사용하면 내부 클래스를 독립적으로 사용 가능)
		String keyword;
	}
}
