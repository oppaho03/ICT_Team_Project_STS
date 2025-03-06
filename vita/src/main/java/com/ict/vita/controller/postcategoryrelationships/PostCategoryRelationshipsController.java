package com.ict.vita.controller.postcategoryrelationships;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.controller.termcategory.TermCategoryController;
import com.ict.vita.repository.posts.PostsRepository;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.EmptyTermRelDto;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postcategories")
public class PostCategoryRelationshipsController {
	//서비스 주입	
	
	private final PostCategoryRelationshipsService postCategoryRelService;
	private final PostsService postsService;

	private final TermsService termsService;
	
	/**
	 * 카테고리 목록 가져오기
	 * @param id 포스트 ID
	 * @return
	 */		
	@Operation(summary = "카테고리 목록 가져오기", description = "카테고리 목록 가져오기")
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":709,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":710,\"name\":\"HIV감염\",\"slug\":\"hiv_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":711,\"name\":\"결핵\",\"slug\":\"tuberculosis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":712,\"name\":\"곰팡이감염\",\"slug\":\"fungal_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681}]}}"
				)
			)
		),
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				examples = @ExampleObject(
					value ="{\"success\":1,\"response\":{\"data\":[]}}"
				)
			)
		) 
	})
	@GetMapping("/post/{id}")
	public ResponseEntity<?> findCategories(@Parameter(description = "포스트 ID") @PathVariable Long id) {

		// 대상 ID 로 관계 검색 
		List<PostCategoryRelationshipsDto> relDtos = postCategoryRelService.findAllByPostId(id);

		if ( relDtos != null && ! relDtos.isEmpty() ) {
			
			List<TermDto> termCatsDto = relDtos.stream().map( dto -> termsService.toTermDto(dto.getTermCategoryDto().toEntity()) ).toList();

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termCatsDto ));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}
	
	/**
	 * 포스트 목록 가져오기
	 * @param id 카테고리 ID
	 * @return
	 */		
	@Operation(summary = "포스트 목록 가져오기", description = "포스트 목록 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = PostsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":1,\"author\":1,\"post_title\":\"테스트글\",\"post_content\":\"\",\"post_summary\":\"\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-05T15:42:18\",\"post_modified_at\":\"2025-03-05T15:42:18\",\"comment_status\":\"OPEN\",\"comment_count\":0}]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/category/{id}")
	public ResponseEntity<?> findPosts(@Parameter(description = "카테고리 ID") @PathVariable Long id) {

		// 대상 ID 로 관계 검색 
		List<PostCategoryRelationshipsDto> relDtos = postCategoryRelService.findAllByTermCategoryId(id);

		if ( relDtos != null && ! relDtos.isEmpty() ) {
			
			List<PostsResponseDto> postsDtos = relDtos.stream().map( dto -> PostsResponseDto.toDto(dto.getPostsDto().toEntity()) ).toList();

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( postsDtos ));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}

	/**
	 * 카테고리 등록 
	 * @param reldto EmptyTermRelDto ( id, categories )
	 * @return
	 */		
	@Operation(summary = "포스트 목록 가져오기", description = "포스트 목록 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":709,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":710,\"name\":\"HIV감염\",\"slug\":\"hiv_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":712,\"name\":\"곰팡이감염\",\"slug\":\"fungal_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":713,\"name\":\"광우병\",\"slug\":\"bovine_spongiform_encephalopathy\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681}]}}"))),
		@ApiResponse(responseCode = "400", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid Values...\"}}")))
	})
	@PostMapping("/")
	public ResponseEntity<?> addCategories(@Parameter( description = "관계 데이터") @RequestBody EmptyTermRelDto reldto ) {
		/* CHECKE AUTH *** */

		Long id = reldto.getId();
		List<Long> categories = reldto.getCategories();

		if ( id == null || id == 0 || categories == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( id == 0 ? "포스트 ID 값으로 NULL 또는 0 을 사용할 수 없습니다." : "카테고리 목록으로 NULL 을 사용할 수 없습니다."  ));
		}
		// < 포스트 ID 유효성 검사 >
		PostsDto postsDto = postsService.findById(id);
		if ( postsDto == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( "유효하지 않은 포스트 ID 입니다." ));
		}

		// < 포스트 ID 로 카테고리 목록 불러오기 >
		List<PostCategoryRelationshipsDto> relDtos = postCategoryRelService.findAllByPostId(id);
		// 현재 포트스가 가지고 있는 전체 목록 
		List<Long> olds = relDtos.stream().map( dto -> dto.getTermCategoryDto().getId() ).toList();

		// 등록, 삭제 할 카테고리 ID 분류
		// - 포스트 + 카테고리 관계 등록 
		Set<Long> adds = new HashSet<>(categories);
		adds.removeAll(olds);
		if ( adds.size() > 0 ) postCategoryRelService.save( postsDto, adds.stream().toList() );

		// - 포스트 + 카테고리 관계 삭제  
		Set<Long> dels = new HashSet<>(olds);
		dels.removeAll(categories);
		if ( dels.size() > 0 ) postCategoryRelService.delete( postsDto, dels.stream().toList() );

		relDtos = postCategoryRelService.findAllByPostId(id);
		System.out.println(relDtos);

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( relDtos.stream().map(dto->TermDto.toDto(dto.getTermCategoryDto().toEntity())).toList() ));
	}


}
