package com.ict.vita.controller.postmeta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.others.ObjectMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termmeta.TermMetaDto;
import com.ict.vita.util.Commons;
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
	private final PostsService postsService;

	// /**
	//  * 검색
	//  * @param meta_id 메타 아이디
	//  * @param meta_key 메타 키
	//  * @param id 오브젝트 ID
	//  * @return ObjectMetaResponseDto | List<ObjectMetaResponseDto> | Null
	//  */	
	// @Operation( summary = "전체 검색", description = "전체 검색" )
	// @ApiResponses({
	// 	@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testvalue\"}}}"))),
	// 	@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":null}}"))),
	// 	@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testmetavalue\"}]}}"))),
	// 	@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	// })
	
	// @GetMapping("/")
	// public ResponseEntity<?> getAll (
	// 	@Parameter(description = "메타 ID", required = false ) @RequestParam(defaultValue = "0") Long meta_id,		
	// 	@Parameter(description = "메타 키", required = false) @RequestParam(defaultValue = "") String meta_key,
	// 	@Parameter(description = "오브젝트 ID", required = false) @RequestParam(defaultValue = "0") Long id
	// ) {
	// 	// < 검색 조건 별 : 메타 정보 반환 >
	// 	if ( meta_id != 0 ) {
	// 		TermMetaDto metaDto = termMetaService.findById(meta_id); 
	// 		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( metaDto == null ? null : ObjectMetaResponseDto.toDto(metaDto.toEntity())  ));
	// 	}
	// 	else if ( id != 0 ) {
			
	// 		// < 검색 조건 : 오브젝트 ID > 
	// 		TermCategoryDto oDto = termsService.findById(id);
	// 		if ( oDto == null ) {
	// 			// 오브젝트가 존재하지 않기 때문에 검색 실패
	// 			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "term.notfound") )); 
	// 		}

	// 		if ( Commons.isNull(meta_key) ) { // - 메타 키 값이 없음, 전체 리스트 반환
	// 			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termMetaService.findAll(oDto).stream().map( dto->ObjectMetaResponseDto.toDto(dto.toEntity()) ).toList() ) );
	// 		}
	// 		else { // - 메타 키 값으로 검색
				
	// 			// meta_key 유효성 검사 
	// 			TermMetaDto metaDto = termMetaService.findByTermsDtoByMetaKey( TermMetaDto.builder().termsDto(oDto.getTermsDto()).meta_key(meta_key).build() );

	// 			if ( metaDto == null ) {
	// 				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "meta.notfound") ));
	// 			}
	// 			else {
	// 				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( metaDto == null ? null : ObjectMetaResponseDto.toDto(metaDto.toEntity())  ));
	// 			}
	// 		}

	// 	}

	// 	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "request.invalid_parameters") ));
	// } 


}
