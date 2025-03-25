package com.ict.vita.controller.posts;


import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsRequestDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@CrossOrigin
public class PostsController {
	
	//서비스 주입
	private final PostsService postsService;
	private final MemberService memberService;
	private final TermsService termService;
	private final PostCategoryRelationshipsService pcrService;
	
	private final MessageSource messageSource;
	
	
	/**
	 * [모든 회원의 공개글 조회] - 가입된 상태(status가 1)인 모든 회원들의 공개글(status가 PUBLISH) 조회 
	 * @param cid 카테고리 id로 필수값
	 * @return ResponseEntity
	*/
	@Operation( summary = "모든 회원 공개글 조회", description = "모든 회원의 공개글 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = PostsResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":68,\"author\":47,\"post_title\":\"수수title123\",\"post_content\":\"수수contents입니다\",\"post_summary\":\"수수정글요약이지롱\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-17T20:53:32.206184\",\"post_modified_at\":\"2025-03-17T21:11:59.990984\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]},{\"id\":69,\"author\":47,\"post_title\":\"글하고질병글입니다.\",\"post_content\":\"contents입니다\",\"post_summary\":\"황조롱이지롱\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-18T17:42:18.876855\",\"post_modified_at\":\"2025-03-18T17:42:18.721348\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]}]}}"
				)
			) 
		)
	})
	@GetMapping
	public ResponseEntity<?> getAllPublicPosts(@Parameter(description = "카테고리 id") @RequestParam("cid") List<Long> cid){
		
		List<PostsDto> dtoList = postsService.getAllPublicPosts(cid, cid.size());
		
		List<TermCategoryDto> categoryDto = termService.findById(cid);
		Set<TermCategoryDto> filter = categoryDto.stream().collect(Collectors.toSet());
		categoryDto = filter.stream().toList();
		List<TermsResponseDto> termsResponses = categoryDto.stream().map(cdto -> TermsResponseDto.toDto(cdto)).toList();
		
		
		List<PostsResponseDto> responseDtoList = dtoList.stream().map(dto -> PostsResponseDto.builder()
													.id(dto.getId())
													.author(dto.getMemberDto().getId())
													.post_title(dto.getPost_title())
													.post_content(dto.getPost_content())
													.post_summary(dto.getPost_summary())
													.post_status(dto.getPost_status())
													.post_pass(dto.getPost_pass())
													.post_name(dto.getPost_name())
													.post_mime_type(dto.getPost_mime_type())
													.post_created_at(dto.getPost_created_at())
													.post_modified_at(dto.getPost_modified_at())
													.comment_status(dto.getComment_status())
													.comment_count(dto.getComment_count())
													.categories( termsResponses )
													.build())
			.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(responseDtoList));
	}
	
	/**
	 * [회원 및 공개/비공개 글 조회]
	 *  <관리자> status 값이 없으면 해당 uid 회원이 작성한 모든 글(공개/비공개) 조회 가능 + status값 넘기면 해당하는 status의 글 조회 가능
	 *  <회원> cid,uid만 값을 넘겨 본인의 공개/비공개 글 조회 가능 + status값 넘기면 해당하는 status의 글 조회 가능
	 * @param cid 카테고리 id로 필수값
	 * @param uid 회원id로 필수값
	 * @param status 글의 status로 옵션값
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	*/
	@Operation( summary = "회원 및 공개/비공개 글 조회", description = "회원 및 공개/비공개 글 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = PostsResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":5,\"author\":29,\"post_title\":\"글제목\",\"post_content\":\"글내용\",\"post_summary\":\"요약\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-10T20:29:38.386\",\"post_modified_at\":\"2025-03-10T20:29:38.386\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]},{\"id\":65,\"author\":29,\"post_title\":\"글제목3\",\"post_content\":\"글내용2\",\"post_summary\":\"요약2\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-11T21:13:12.195243\",\"post_modified_at\":\"2025-03-11T21:13:12.190857\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]}]}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "401-조회 실패",
				description = "FAIL(회원 미존재)",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/member")
	public ResponseEntity<?> getPostsByUser(
			@Parameter(description = "카테고리id") @RequestParam("cid") Long cid,
			@Parameter(description = "조회할 회원id") @RequestParam("uid") Long uid,
			@Parameter(description = "글의 상태") @RequestParam(value = "status",required = false) String status,
			@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		//[로그인한 사람이 관리자/일반회원인지 확인]
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);//로그인한 회원
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
		TermCategoryDto category = termService.findById(cid);
		//카테고리가 존재하지 않는 경우
		if(category == null) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(Arrays.asList()));
		}

		
		MemberDto searchMember = memberService.findMemberById(uid); //조회하고자 하는 회원
		
		//<관리자거나 본인이 쓴 글 조회시>
		if( (loginMember.getId() == searchMember.getId() || Commons.ROLE_ADMINISTRATOR.equals(loginMember.getRole()) ) ) {	
			if(!Commons.isNull(status)) {
				//status에 해당하는 회원의 게시글 조회
				List<PostsResponseDto> postsList = postsService.getPostsByMemberAndStatus(cid,uid,status)
											.stream().map(dto -> PostsResponseDto.toDto(dto.toEntity(), List.of(TermsResponseDto.toDto(category)) )).toList();
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
			}
			//status값을 넘기지 않은 경우 - 공개/비공개 모든 글 조회
			List<PostsResponseDto> postsList = postsService.getPostsByMember(cid, uid)
											.stream().map(dto -> PostsResponseDto.toDto(dto.toEntity(), List.of(TermsResponseDto.toDto(category)) )).toList();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
		}	
		
		//<로그인한 회원이 글 작성자가 아닌 경우>
		//status값을 null 또는 공개글 조회가 아닌 경우
		if ( Commons.isNull(status) || ! status.equalsIgnoreCase(Commons.POST_STATUS_PUBLISH) ) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(List.of()));	
		}
		
		else {
			List<PostsResponseDto> postsList = postsService.getPostsByMemberAndStatus(cid,uid,status)
						.stream().map(dto->PostsResponseDto.toDto(dto.toEntity(),List.of(TermsResponseDto.toDto(category)) )).toList();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
		}

	}
	
	/**
	 * [제목 및 회원 아이디로 글 검색]
	 * title 전달시 제목으로 검색
	 * nickname 전달시 닉네임으로 해당 회원의 모든 공개글 검색
	 * @param cid 카테고리id(필수값)
	 * @param title 검색하고자 하는 글 제목(옵션값)
	 * @param nickname 검색하고자 하는 회원 닉네임(옵션값)
	 * @return ResponseEntity
	 */
	@Operation( summary = "제목 및 회원 아이디로 글 검색", description = "제목 및 회원 아이디로 글 검색 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = PostsResponseDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":5,\"author\":29,\"post_title\":\"글제목\",\"post_content\":\"글내용\",\"post_summary\":\"요약\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-10T20:29:38.386\",\"post_modified_at\":\"2025-03-10T20:29:38.386\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]},{\"id\":65,\"author\":29,\"post_title\":\"글제목3\",\"post_content\":\"글내용2\",\"post_summary\":\"요약2\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-11T21:13:12.195243\",\"post_modified_at\":\"2025-03-11T21:13:12.190857\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0}]}]}}"
				)
			) 
		)
	})
	@GetMapping("/s")
	public ResponseEntity<?> searchByTitleOrMember(
			@Parameter(description = "카테고리id") @RequestParam("cid") Long cid, 
			@Parameter(description = "검색할 게시글 제목") @RequestParam(value = "title",required = false) String title, 
			@Parameter(description = "검색할 회원 닉네임") @RequestParam(value = "nickname",required = false) String nickname){
		
		TermCategoryDto category = termService.findById(cid);
		//카테고리가 존재하지 않는 경우
		if(category == null) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(List.of()));
		}
		
		//제목 또는 닉네임 전달시
		if(!Commons.isNull(title) || !Commons.isNull(nickname)) {		
			List<PostsDto> postsList = null;
			if( !Commons.isNull(title) ) postsList = postsService.getPostsByTitle(cid,title);
			else postsList = postsService.getPostsByNickname(cid, nickname);
			
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList.stream().map(dto->PostsResponseDto.toDto(dto.toEntity(),List.of(TermsResponseDto.toDto(category)) )).toList()));
			
		}

		//제목 또는 닉네임 미전달시
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(List.of()));
		
		
	}

	/**
	 * [글 등록]
	 * @param token 로그인한 회원의 토큰값
	 * @param postsRequestDto 등록하려는 글의 정보
	 * @return ResponseEntity
	 */
	@Operation( summary = "글 등록", description = "글 등록 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "201-글 등록 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = PostsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":71,\"author\":49,\"post_title\":\"우하하하하\",\"post_content\":\"글글글\",\"post_summary\":\"우하하하글\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-21T11:52:10.7016797\",\"post_modified_at\":\"2025-03-21T11:52:10.7016797\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":830,\"name\":\"구순염\",\"slug\":\"cheilitis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":825}]}}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-글 등록 실패",
				description = "FAIL",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"글등록실패했습니다\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "401-글 등록 실패",
				description = "FAIL(회원 미존재)",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
					)
				) 
			)
	})
	@PostMapping
	public ResponseEntity<?> createPost(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "글 등록 dto") @RequestBody PostsRequestDto postsRequestDto
			){

		//토큰값으로 로그인한 회원 확인
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		
				
		//존재하는 카테고리에 대해서 글-카테고리 관계 테이블에 삽입	
		PostsDto post = postsRequestDto.toDto();	
		post.setMemberDto(loginMember);
		Set<Long> filter = postsRequestDto.getCids().stream().collect(Collectors.toSet());
		List<Long> categories = filter.stream().collect(Collectors.toList());
		
		//글 저장
		PostsDto savedPost = postsService.savePost(post);
				
		//글-카테고리 관계 저장
		if(savedPost != null && pcrService.save(savedPost, categories)) {		 			
			 List<TermCategoryDto> termCategoryDtos = termService.findById(categories);
			 List<TermsResponseDto> termCategoryResponseDtos = termCategoryDtos.stream().map(dto->TermsResponseDto.toDto(dto)).toList();
			return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success(PostsResponseDto.toDto(savedPost.toEntity(), termCategoryResponseDtos ) ));
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("글 등록 실패했습니다"));	
		
	}
	
	/**
	 * [글 수정]
	 * @param token 회원 토큰값
	 * @param pid 글 id
	 * @param updateRequestDto 수정할 글 정보
	 * @return ResponseEntity
	 */
	@Operation( summary = "글 수정", description = "글 수정 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-글 수정 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = PostsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":70,\"author\":47,\"post_title\":\"수수정22\",\"post_content\":\"22수contents입니다\",\"post_summary\":\"22수정글요약이지롱\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-19T21:02:49.671588\",\"post_modified_at\":\"2025-03-21T11:58:44.2114677\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[{\"id\":816,\"name\":\"글\",\"slug\":\"post\",\"group_number\":0,\"category\":\"post\",\"description\":null,\"count\":0,\"parent\":0},{\"id\":825,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0}]}}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-글 수정 실패",
				description = "FAIL",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"글수정실패했습니다\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "401-글 수정 실패",
				description = "FAIL(회원 미존재)",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "403-글 수정 실패",
				description = "FAIL(글작성자 아님)",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"글작성자만수정가능합니다\"}}"
					)
				) 
			)
	})
	@PutMapping("/{pid}")
	public ResponseEntity<?> editPost(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "수정할 글 id") @PathVariable("pid") Long pid,
			@Parameter(description = "글 수정 dto") @RequestBody PostsRequestDto updateRequestDto)
	{
		//로그인한 회원 확인
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		//글 조회
		PostsDto findedPost = postsService.findById(pid);
		//글 작성자인 경우
		if(findedPost.getMemberDto().getId() == loginMember.getId()) {
			findedPost.setPost_title(updateRequestDto.getPost_title());
			findedPost.setPost_content(updateRequestDto.getPost_content());
			findedPost.setPost_summary(updateRequestDto.getPost_summary());
			findedPost.setPost_status(updateRequestDto.getPost_status());
			findedPost.setPost_pass(updateRequestDto.getPost_pass());
			findedPost.setPost_name(updateRequestDto.getPost_name());
			findedPost.setPost_mime_type(updateRequestDto.getPost_mime_type());
			findedPost.setPost_modified_at(updateRequestDto.getPost_modified_at());
			findedPost.setComment_status(updateRequestDto.getComment_status());
			findedPost.setComment_count(updateRequestDto.getComment_count());
			
			Set<Long> filter = updateRequestDto.getCids().stream().collect(Collectors.toSet());
			List<Long> categories = filter.stream().toList();
			
			//글 저장
			PostsDto savedPost = postsService.savePost(findedPost);
			//글-카테고리 관계 수정
			if ( savedPost != null ) {
				List<PostCategoryRelationshipsDto> pcrDtos = pcrService.update(savedPost, categories);
				List<TermsResponseDto> termsResponseDto = pcrDtos.stream().map( pcrDto->TermsResponseDto.toDto(pcrDto.getTermCategoryDto()) ).toList();
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(PostsResponseDto.toDto(savedPost.toEntity(), termsResponseDto)));
			}		
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("글 수정 실패했습니다"));
		}
		
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail("글 작성자만 수정 가능합니다"));
	}
	
	/**
	 * [글 삭제]
	 * @param token 회원 토큰값
	 * @param pid 삭제할 글 id
	 * @return ResponseEntity
	 */
	@Operation( summary = "글 삭제", description = "글 삭제 API" )
	@ApiResponses({
		@ApiResponse( 
				responseCode = "200-글 삭제 성공",
				description = "SUCCESS",
				content = @Content(	
					schema = @Schema(implementation = PostsResponseDto.class),
					examples = @ExampleObject(
						value = "{\"success\":1,\"response\":{\"data\":{\"id\":63,\"author\":29,\"post_title\":\"글제목2\",\"post_content\":\"글내용2\",\"post_summary\":\"요약2\",\"post_status\":\"DELETE\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-11T21:06:55.460514\",\"post_modified_at\":\"2025-03-11T21:06:55.450154\",\"comment_status\":\"OPEN\",\"comment_count\":0,\"categories\":[]}}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "401-글 삭제 실패",
				description = "FAIL(회원 미존재)",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
				responseCode = "403-글 삭제 실패",
				description = "FAIL",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"글작성자만삭제가능합니다\"}}"
					)
				) 
			),
		
	})
	@DeleteMapping("/{pid}")
	public ResponseEntity<?> deletePost(
			@Parameter(description = "로그인한 회원 토큰") @RequestHeader("Authorization") String token,
			@Parameter(description = "삭제할 글 id") @PathVariable("pid") Long pid){
		//로그인한 회원 확인
		MemberDto loginMember = Commons.findMemberByToken(token, memberService);
		//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}
		//글 조회
		PostsDto findedPost = postsService.findById(pid);
		
		//글 존재하지 않거나 글이 삭제된 경우
		if( findedPost == null || Commons.POST_STATUS_DELETE.equals(findedPost.getPost_status()) ) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(null));
		}
	
		//글 작성자가 아닌 경우
		if(findedPost.getMemberDto().getId() != loginMember.getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail( "글 작성자만 삭제 가능합니다" ));
		}
		
		PostsDto deletedPost = postsService.deletePost(findedPost.getId());
		
		List<PostCategoryRelationshipsDto> pcrDtos = pcrService.findAllByPostId(pid);
		List<TermsResponseDto> responses = pcrDtos.stream().map(dto -> TermsResponseDto.toDto(dto.getTermCategoryDto())).toList();
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(PostsResponseDto.toDto(deletedPost.toEntity(),responses )) );

	}
	
	
}
