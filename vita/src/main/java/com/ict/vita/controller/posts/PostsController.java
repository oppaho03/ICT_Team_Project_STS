package com.ict.vita.controller.posts;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
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
public class PostsController {
	//서비스 주입
	private final PostsService postsService;
	private final MemberService memberService;
	
	
	/**
	 * [모든 회원의 공개글 조회] - 가입된 상태(status가 1)인 모든 회원들의 공개글(status가 PUBLISH) 조회 
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
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":1,\"author\":24,\"post_title\":\"글제목\",\"post_content\":\"글내용\",\"post_summary\":\"요약\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-10T08:23:17.509\",\"post_modified_at\":\"2025-03-10T08:23:17.509\",\"comment_status\":\"OPEN\",\"comment_count\":0}]}}" 
				)
			) 
		)
	})
	@GetMapping
	public ResponseEntity<?> getAllPublicPosts(){
		List<PostsDto> dtoList = postsService.getAllPublicPosts();
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
													.build())
						.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(responseDtoList));
	}
	
	/**
	 * [회원 및 공개/비공개 별 글 조회]
	 *  <관리자> status 값이 없으면 해당 uid 회원이 작성한 모든 글(공개/비공개) 조회 가능 
	 *  <회원> uid만 값을 넘겨 본인의 공개/비공개 글 조회 가능
	 * @param uid 회원id로 필수값
	 * @param status 글의 status로 옵션값
	 * @param token 로그인한 회원의 토큰값
	 * @return ResponseEntity
	*/
	@GetMapping("/user")
	public ResponseEntity<?> getPostsByUser(@RequestParam Long uid,@RequestParam(required = false) String status,@RequestHeader String token){
		//로그인한 회원의 토큰값으로 회원 조회
		MemberDto findedMember = memberService.findMemberByToken(token);
		
		//파라미터로 uid 값은 전달하고 status 값은 전달하지 않은 경우 <관리자인 경우> - 해당 회원의 모든 게시글 조회
		if(Commons.isNull(status)) {
			//해당하는 회원의 모든 게시글을 조회하는 메서드 호출
			List<PostsDto> findedPosts = postsService.getPostsByMember(uid);
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(findedPosts));
		}
		
		//파라미터로 uid,status 값을 전달한 경우 <회원인 경우>
		
		//로그인한 회원(findedMember)이 본인이 작성하지 않은 공개글 이외의 글 조회 요청시
		if(findedMember.getId() != uid && !status.equals(Commons.POST_STATUS_PUBLISH)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResultUtil.fail("타 회원의 공개글만 조회 가능합니다"));
		}
		
		//로그인한 회원이 자신이 쓴 글 조회 요청시
		List<PostsDto> postsList = postsService.getPostsByMemberAndStatus(uid, status);
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));

	}
	
	//글의 status별 글 조회
	
	
}
