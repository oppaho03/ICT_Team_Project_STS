package com.ict.vita.controller.postcategoryrelationships;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.postmeta.PostMetaDto;
import com.ict.vita.service.postmeta.PostMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.others.ObjectCategoryRelDto;
import com.ict.vita.service.terms.TermsResponseDto;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/postcategories")
@CrossOrigin
public class PostCategoryRelationshipsController {
	//서비스 주입	
	private final PostCategoryRelationshipsService postCategoryRelService;
	private final PostsService postsService;
	private final MemberService memberService;
	private final TermCategoryService categoryService;
	private final PostMetaService postMetaService;
	private final MemberMetaService memberMetaService;
	
	private final MessageSource messageSource;

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
				schema = @Schema(implementation = TermsResponseDto.class),
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

			//TermsResponseDto
			List<TermsResponseDto> result = relDtos.stream().map( dto -> TermsResponseDto.toDto(dto.getTermCategoryDto().toEntity()) ).toList();
			

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}
	
	/**
	 * 포스트 목록 가져오기
	 * @param id 카테고리 ID
	 * @param p 페이지
	 * @param ol 출력 개수 제한
	 * @return
	 */		
	@Operation(summary = "포스트 목록 가져오기", description = "포스트 목록 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
				schema = @Schema(implementation = PostsResponseDto.class), 
				examples = @ExampleObject(
						value = "{\"success\":1,\"response\":{\"data\":[{\"id\":65,\"author\":{\"id\":29,\"email\":\"oppaho123@gmail.com\",\"name\":\"홍길동\",\"nickname\":\"oppaho123\",\"birth\":\"2025-02-27\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoib3BwYWhvMTIzQGdtYWlsLmNvbSIsInN1YiI6IjI5IiwiaWF0IjoxNzQyNTI3MTkzLCJleHAiOjE3NDI1MjgwOTN9.C6HAZD0g8emfFRb-6Em7_DSqI6HGd8sbGSTpWf5WmQE\",\"created_at\":\"2025-02-27T20:28:06.48291\",\"updated_at\":\"2025-02-27T20:28:06.475567\",\"status\":1,\"meta\":[]},\"post_title\":\"글제목3\",\"post_content\":\"글내용2\",\"post_summary\":\"요약2\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-11T21:13:12.195243\",\"post_modified_at\":\"2025-03-11T21:13:12.190857\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[]},{\"id\":67,\"author\":{\"id\":47,\"email\":\"baekjongwon1@naver.com\",\"name\":\"박종원원\",\"nickname\":\"빽다방\",\"birth\":\"1916-03-15\",\"gender\":\"M\",\"contact\":null,\"address\":null,\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoiYmFla2pvbmd3b24xQG5hdmVyLmNvbSIsInN1YiI6IjQ3IiwiaWF0IjoxNzQyMTcwNzM1LCJleHAiOjE3NDIxNzE2MzV9.evCR4S-62afBMxdsoJ_QxqwIfwm4tqCbJXtCHuQzkFI\",\"created_at\":\"2025-03-15T13:58:21.560432\",\"updated_at\":\"2025-03-17T09:32:32.773729\",\"status\":1,\"meta\":[]},\"post_title\":\"title123\",\"post_content\":\"contents입니다\",\"post_summary\":\"글요약이지롱\",\"post_status\":\"DELETE\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-17T20:52:30.204812\",\"post_modified_at\":\"2025-03-17T20:52:30.194811\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[]}]}}" 
				))
		),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
				examples = @ExampleObject(
						value = "{\"success\":1,\"response\":{\"data\":[]}}"
				))
		)
	})
	@GetMapping("/category/{id}")
	public ResponseEntity<?> findPosts(
		@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
		@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol,
		@Parameter(description = "카테고리 ID") @PathVariable Long id		
	) {

		// 대상 ID 로 관계 검색 
		List<PostCategoryRelationshipsDto> relDtos = null;

		if ( p > 0 ) relDtos = postCategoryRelService.findAllByTermCategoryId(id, p, ol); 
		else relDtos = postCategoryRelService.findAllByTermCategoryId(id);

		if ( relDtos != null && ! relDtos.isEmpty() ) {
			TermCategoryDto category = categoryService.findById(id);

			List<PostsResponseDto> postsDtos = relDtos.stream()
					.map( rel -> {
							//글 작성자 메타정보
							MemberDto postMember = rel.getPostsDto().getMemberDto();
							List<MemberMetaResponseDto> memberMeta = memberMetaService.findAll(postMember)
																	.stream()
																	.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																	.toList();
						
							return PostsResponseDto.toDto(
										rel.getPostsDto().toEntity(), 
										List.of(TermsResponseDto.toDto(category)),
										postMetaService.findAll(rel.getPostsDto()).stream().map( meta -> PostMetaResponseDto.toResponseDto(meta) ).toList(),
										memberMeta); 
					})
					.toList();		

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( postsDtos ));
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}

	/**
	 * 카테고리 등록 
	 * @param reldto ObjectCategoryRelDto ( id, categories )
	 * @return
	 */		
	@Operation(summary = "포스트 목록 가져오기", description = "포스트 목록 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":709,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":710,\"name\":\"HIV감염\",\"slug\":\"hiv_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":712,\"name\":\"곰팡이감염\",\"slug\":\"fungal_infection\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681},{\"id\":713,\"name\":\"광우병\",\"slug\":\"bovine_spongiform_encephalopathy\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681}]}}"))),
		@ApiResponse(responseCode = "400", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid Values...\"}}")))
	})
	@PostMapping("/")
	public ResponseEntity<?> addCategories(
		@RequestHeader(value = "Authorization", required = true) String token,
		@Parameter( description = "관계 데이터") @RequestBody ObjectCategoryRelDto reldto 
	) {
		// < JWT Token 유효성 검사 >
		MemberDto user = Commons.findMemberByToken(token, memberService);
		if ( user == null ) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}

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

		// < 포스트 ID 로 카테고리 목록 비교 및 추가 / 삭제 >
		List<PostCategoryRelationshipsDto> relDtos = postCategoryRelService.update(postsDto, categories);

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( relDtos.stream().map(dto->TermsResponseDto.toDto(dto.getTermCategoryDto().toEntity())).toList() ));
	}


}
