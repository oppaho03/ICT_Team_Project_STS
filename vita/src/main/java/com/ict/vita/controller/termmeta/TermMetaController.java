package com.ict.vita.controller.termmeta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.others.ObjectMetaDto;
import com.ict.vita.service.termmeta.TermMetaService;
import com.ict.vita.service.terms.TermDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms/meta")
public class TermMetaController {
	//서비스 주입
	private final TermMetaService termMetaService;
	private final TermsService termsService;
	
	/**
	 * 전체 검색 (카테고리 ID)
	 * @param obj_id TermCategory ID
	 * @return 메타 리스트 반환
	 */	
	@Operation( summary = "전체 검색", description = "전체 검색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"meta_id\":1,\"obj_id\":681,\"meta_key\":\"_test\",\"meta_value\":\"testvalue\"}]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/by_category/{obj_id}")
	public ResponseEntity<?> getAll (
		@Parameter(description = "카테고리 ID") @PathVariable Long obj_id 
	) {
		TermDto term = termsService.findById(obj_id);
		List<ObjectMetaDto> result = new ArrayList<>();

		if ( term != null ) 
			result.addAll( termMetaService.findAll(term) );
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));
	} 

	/**
	 * ID 검색
	 * @param id 메타 ID
	 * @return 메타 또는 NULL 반환
	 */	
	@Operation( summary = "전체 검색", description = "전체 검색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"meta_id\":1,\"obj_id\":681,\"meta_key\":\"_test\",\"meta_value\":\"testvalue\"}]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/{id}")
	public ResponseEntity<?> getById (
		@Parameter(description = "메타 ID") @PathVariable Long id 
	) {
		ObjectMetaDto result = termMetaService.findById(id);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));
	} 


	
	/**
	 * 등록  
	 * @param meta MetaDto
	 * @return 메타 값 반환
	 */	
	@Operation( summary = "메타 등록", description = "메타 등록" )
	public ResponseEntity<?> add () {

		
		return null;
	} 

}
