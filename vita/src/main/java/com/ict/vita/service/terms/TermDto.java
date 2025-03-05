package com.ict.vita.service.terms;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class TermDto {
    @Schema(description = "ID", example = "0")	
    @NotBlank(message = "")
    private Long id; // APP_TERM_CATEGORY.id
    @Schema(description = "Term ID", example = "0")	
    @NotBlank(message = "")
    private Long term_id; // APP_TERM.id
    @Schema(description = "이름", example = "")	
    private String name; // APP_TERM.name
    @Schema(description = "슬러그", example = "")
	private String slug; // APP_TERM.slug
    @Schema(description = "그룹 번호", example = "0")
    private long group_number; // APP_TERM.group_number
    @Schema(description = "카테고리명(Taxonomy)", example = "department")
    private String category; // APP_TERM_CATEGORY.category
    @Schema(description = "설명", example = "")
    private String description; // APP_TERM_CATEGORY.description
    @Schema(description = "소속된 컨텐츠 수", example = "0")
    private Long count = 0L; // APP_TERM_CATEGORY.count
    @Schema(description = "소속된 컨텐츠 수", example = "0")
	private Long parent = 0L; // APP_TERM.id
}
