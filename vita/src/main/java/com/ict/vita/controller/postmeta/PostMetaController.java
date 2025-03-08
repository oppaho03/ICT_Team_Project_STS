package com.ict.vita.controller.postmeta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.others.ObjectMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termmeta.TermMetaDto;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/meta")
public class PostMetaController {
	//서비스 주입
	private final PostMetaService postMetaService;

	// /**
	//  * 전체 검색 (포스트 ID)
	//  * @param id 포스트 ID
	//  * @return 메타 리스트 반환
	//  */	
	// @Operation( summary = "전체 검색", description = "전체 검색" )
	// @ApiResponses({
	// 	@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value ="{\"success\":1,\"response\":{\"data\":[{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testvalue\"}]}}"))),
	// 	@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	// })
	// @GetMapping("/by_posy/{id}")
	// public ResponseEntity<?> getAll (
	// 	@Parameter(description = "카테고리 ID") @PathVariable Long id 
	// ) {

		
	// 	// // TermCategoryDto term = termsService.findById(id);
	// 	// List<TermMetaDto> result = new ArrayList<>();

	// 	// if ( term != null ) 
	// 	// 	result.addAll( termMetaService.findAll(term) );
		
	// 	// return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));

	// 	return null;
	// } 
	
	
}
