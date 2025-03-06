package com.ict.vita.service.terms;

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
public class EmptyTermRelDto {
    @Schema(description = "대상 ID", example = "0")	
    private Long id = 0L; 

    @Schema(description = "카테고리 ID 리스트", example = "null")	
    private List<Long> categories = null; 
}
