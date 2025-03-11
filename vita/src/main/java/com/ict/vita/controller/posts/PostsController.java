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
public class PostsController {
	//서비스 주입
	private final PostsService postsService;
	private final MemberService memberService;
	
	
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
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":1,\"author\":24,\"post_title\":\"글제목\",\"post_content\":\"글내용\",\"post_summary\":\"요약\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-10T08:23:17.509\",\"post_modified_at\":\"2025-03-10T08:23:17.509\",\"comment_status\":\"OPEN\",\"comment_count\":0}]}}" 
				)
			) 
		)
	})
	@GetMapping
	public ResponseEntity<?> getAllPublicPosts(@Parameter(description = "카테고리 id") @RequestParam("cid") Long cid){
		List<PostsDto> dtoList = postsService.getAllPublicPosts(cid);
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
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":5,\"author\":29,\"post_title\":\"글제목\",\"post_content\":\"글내용\",\"post_summary\":\"요약\",\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":null,\"post_mime_type\":null,\"post_created_at\":\"2025-03-10T20:29:38.386\",\"post_modified_at\":\"2025-03-10T20:29:38.386\",\"comment_status\":\"OPEN\",\"comment_count\":0}]}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-조회 실패",
				description = "FAIL",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"작성자만비공개글조회가능합니다\"}}"
					)
				) 
			)
	})
	@GetMapping("/user")
	public ResponseEntity<?> getPostsByUser(@Parameter(description = "카테고리id") @RequestParam("cid") Long cid,@Parameter(description = "조회할 회원id") @RequestParam("uid") Long uid,
			@Parameter(description = "글의 상태") @RequestParam(value = "status",required = false) String status,@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		//[로그인한 사람이 관리자/일반회원인지 확인]
		MemberDto loginMember = memberService.findMemberByToken(token); //로그인한 회원
		//<<로그인한 회원이 관리자인 경우>>
		if(Commons.ROLE_ADMINISTRATOR.equals(loginMember.getRole())) {
			//status값을 넘긴 경우 - 해당하는 status의 글 조회
			if(!Commons.isNull(status)) {
				//status에 해당하는 회원의 게시글 조회
				List<PostsResponseDto> postsList = postsService.getPostsByMemberAndStatus(cid,uid,status)
											.stream().map(dto -> PostsResponseDto.toDto(dto.toEntity())).toList();
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
			}		
			//status값을 넘기지 않은 경우 - 공개/비공개 모든 글 조회
			List<PostsResponseDto> postsList = postsService.getPostsByMember(cid, uid)
											.stream().map(dto -> PostsResponseDto.toDto(dto.toEntity())).toList();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
		}
		
		//<<로그인한 회원이 일반회원인 경우>>
		MemberDto searchMember = memberService.findMemberById(uid); //조회하고자 하는 회원
		//로그인한 회원이 글 작성자인 경우
		if(loginMember.getId() == searchMember.getId()) {
			
			//status값을 넘긴 경우 - 해당하는 status의 글 조회
			if(!Commons.isNull(status)) {
				//status에 해당하는 회원의 게시글 조회
				List<PostsResponseDto> postsList = postsService.getPostsByMemberAndStatus(cid,uid,status)
						.stream().map(dto->PostsResponseDto.toDto(dto.toEntity())).toList();
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
			}	
			//status값을 넘기지 않은 경우 - 공개/비공개 모든 글 조회
			List<PostsResponseDto> postsList = postsService.getPostsByMember(cid, uid)
						.stream().map(dto->PostsResponseDto.toDto(dto.toEntity())).toList();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
		}
		
		//로그인한 회원이 글 작성자가 아닌 경우
		//status값을 null 또는 공개글 조회가 아닌 경우
		if ( Commons.isNull(status) || ! status.equalsIgnoreCase(Commons.POST_STATUS_PUBLISH) ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail("작성자만 비공개글 조회 가능합니다"));	
		}
		else {
			List<PostsResponseDto> postsList = postsService.getPostsByMemberAndStatus(cid,uid,status)
						.stream().map(dto->PostsResponseDto.toDto(dto.toEntity())).toList();
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
	@GetMapping("/s")
	public ResponseEntity<?> searchByTitleOrMember(
			@RequestParam("cid") Long cid, 
			@RequestParam(value = "title",required = false) String title, 
			@RequestParam(value = "nickname",required = false) Long nickname){
		//제목으로 검색(title 전달시)
		if(!Commons.isNull(title)) {
			List<PostsResponseDto> postsList = postsService.getPostsByTitle(cid,title)
					.stream().map(dto->PostsResponseDto.toDto(dto.toEntity())).toList();
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsList));
		}
		//닉네임으로 검색(nickname 전달시)
		
	}

}
