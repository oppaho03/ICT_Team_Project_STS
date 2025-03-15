package com.ict.vita.service.others;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
//[포스트나 답변 의 카테고리를 받는 DTO]
public class ObjectCategoryRelDto {
    @Schema(description = "대상 ID", example = "0")	
    private Long id = 0L;  //포스트나 답변의 id(PK)

    @Schema(description = "카테고리 ID 리스트", example = "null")	
    private List<Long> categories = null; 
}
