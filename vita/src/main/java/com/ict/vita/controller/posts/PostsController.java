package com.ict.vita.controller.posts;


import java.util.List;
import java.util.Map;

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
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.ResultUtil;

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
	//
	@GetMapping
	public ResponseEntity<?> getAllPublicPosts(){
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(postsService.getAllPublicPosts()));
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
