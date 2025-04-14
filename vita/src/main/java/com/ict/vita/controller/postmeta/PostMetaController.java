package com.ict.vita.controller.postmeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.others.ObjectMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaDto;
import com.ict.vita.service.postmeta.PostMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.postmeta.SarResultDto;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termmeta.TermMetaDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/meta")
@CrossOrigin
public class PostMetaController {

	//서비스 주입
	private final MemberService memberService;
	
	private final MessageSource messageSource;

	private final PostMetaService postMetaService;
	private final PostsService postsService;

	/**
	 * 검색
	 * @param meta_id 메타 아이디
	 * @param meta_key 메타 키
	 * @param id 오브젝트 ID
	 * @return ObjectMetaResponseDto | List<ObjectMetaResponseDto> | Null
	 */	
	@Operation( summary = "전체 검색", description = "전체 검색" )

	@GetMapping("/")
	public ResponseEntity<?> getAll (
		@Parameter(description = "메타 ID", required = false ) @RequestParam(defaultValue = "0") Long meta_id,		
		@Parameter(description = "메타 키", required = false) @RequestParam(defaultValue = "") String meta_key,
		@Parameter(description = "오브젝트 ID", required = false) @RequestParam(defaultValue = "0") Long id
	) {
		// < 검색 조건 별 : 메타 정보 반환 >
		if ( meta_id != 0 ) {
			PostMetaDto metaDto = postMetaService.findById(meta_id); 
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( metaDto == null ? null : ObjectMetaResponseDto.toDto(metaDto.toEntity())  ));
		}
		else if ( id != 0 ) {
			
			// < 검색 조건 : 오브젝트 ID > 
			PostsDto oDto = postsService.findById(id);
			if ( oDto == null ) {
				// 오브젝트가 존재하지 않기 때문에 검색 실패
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "post.notfound") )); 
			}

			if ( Commons.isNull(meta_key) ) { // - 메타 키 값이 없음, 전체 리스트 반환
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( postMetaService.findAll(oDto).stream().map( dto->ObjectMetaResponseDto.toDto(dto.toEntity()) ).toList() ) );
			}
			else { // - 메타 키 값으로 검색
			
				// meta_key 유효성 검사 
				PostMetaDto metaDto = postMetaService.findByPostDtoByMetaKey( PostMetaDto.builder().postsDto(oDto).meta_key(meta_key).build() );

				if ( metaDto == null ) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "meta.notfound") ));
				}
				else {
					return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( metaDto == null ? null : ObjectMetaResponseDto.toDto(metaDto.toEntity())  ));
				}
			}

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "request.invalid_parameters") ));
	} 

	/**
	 * 등록  
	 * @param dto ObjectMetaDto
	 * @return 메타 값 반환
	 */	
	@Operation( summary = "메타 등록", description = "메타 등록" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testmetavalue\"}}}"))),
		@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	})
	@PostMapping("/")
	public ResponseEntity<?> add (  
		@RequestHeader(value = "Authorization", required = true) String token,
		@Parameter( description = "데이터" ) @RequestBody( required = true ) @Valid ObjectMetaRequestDto dto,
		BindingResult bindingResult 
	) {

		// < JWT Token 유효성 검사 >
		MemberDto user = Commons.findMemberByToken(token, memberService);
		if ( user == null ) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}

		// < DTO 객체 필드의 유효성 검증 실패시 >
		String content = Commons.formatBindingResultHasError(bindingResult, "메타 유효성 검증 실패");
		if ( content != null ) System.out.println( content );

		// 오류 메시지 
		String errmsg = null;

		// 값 유효성 체크 
		Long id = dto.getId();
		String meta_key = dto.getMeta_key();
		String meta_value = dto.getMeta_value();

		if ( id == 0 || Commons.isNull(meta_key) ) {	
			errmsg = Commons.i18nMessages(messageSource, "meta.key.empty");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail(errmsg  ));
		}
		else if ( Commons.isNull(meta_value) ) meta_value = "";

		// 포스트 ID 유효성 검사
		PostsDto post = postsService.findById( id );
		if ( post != null ) {

			dto.setMeta_value(meta_value);

			PostMetaDto result = postMetaService.save(dto); 
			if ( result != null ) 
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( ObjectMetaResponseDto.toDto(result.toEntity()) ));

		}
		else errmsg = Commons.i18nMessages(messageSource, "post.notfound");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail(errmsg  ));	
	}
	
	/**
	 * [음성분석결과 관련 글 메타 정보 조회]
	 * @param token 회원 토큰값
	 * @return
	 */
	@Operation( summary = "음성분석결과 관련 글 메타 정보 조회", description = "음성분석결과 관련 글 메타 정보 조회 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-음성분석결과 관련 글 메타 정보 조회 성공",
			description = "SUCCESS",
			content = @Content(	
				array = @ArraySchema(
						schema = @Schema(implementation = SarResultDto.class)
				),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"post\":{\"id\":213,\"author\":{\"id\":110,\"email\":\"admin@gmail.com\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-04-13\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiMTEwIiwiaWF0IjoxNzQ0NjAzOTc1LCJleHAiOjE3NDQ2MDQ4NzV9.fP9bEfywEj0CdyNhRjrGJSyOMV1-uSqNvNTE36G9DDs\",\"created_at\":\"2025-04-13T12:00:55.646\",\"updated_at\":\"2025-04-13T12:00:55.646\",\"status\":1,\"meta\":[{\"meta_id\":11,\"meta_key\":\"picture\",\"meta_value\":\"https://yt3.googleusercontent.com/HCv0fXFEEcD0HRyF0_qR1K7b7qO3KCzmIoyH1DEJYB94CIUFhIE5i2t2IDIPX97W1-DK4hegww=s160-c-k-c0x00ffffff-no-rj\"}]},\"post_title\":\"20250413131106_1HZeVp6Jry.ogg\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"20250413131106_1HZeVp6Jry.ogg\",\"post_mime_type\":\"audio/ogg;codecs=opus\",\"post_created_at\":\"2025-04-13T13:11:05.232067\",\"post_modified_at\":\"2025-04-13T13:11:05.229177\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":536,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131106_1HZeVp6Jry.ogg\"},{\"id\":537,\"key\":\"sar_file_name\",\"value\":\"20250413131106_1HZeVp6Jry.ogg\"},{\"id\":538,\"key\":\"sar_transcribed_text\",\"value\":\"기듬의주요정상은무엇인가요?\"},{\"id\":539,\"key\":\"sar_overall_sentiment\",\"value\":\"POSITIVE\"},{\"id\":540,\"key\":\"sar_overall_score\",\"value\":\"0.53311396\"},{\"id\":541,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"}]},\"meta\":[{\"id\":536,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131106_1HZeVp6Jry.ogg\"},{\"id\":537,\"key\":\"sar_file_name\",\"value\":\"20250413131106_1HZeVp6Jry.ogg\"},{\"id\":538,\"key\":\"sar_transcribed_text\",\"value\":\"기듬의주요정상은무엇인가요?\"},{\"id\":539,\"key\":\"sar_overall_sentiment\",\"value\":\"POSITIVE\"},{\"id\":540,\"key\":\"sar_overall_score\",\"value\":\"0.53311396\"},{\"id\":541,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"}]},{\"post\":{\"id\":214,\"author\":{\"id\":110,\"email\":\"admin@gmail.com\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-04-13\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiMTEwIiwiaWF0IjoxNzQ0NjAzOTc1LCJleHAiOjE3NDQ2MDQ4NzV9.fP9bEfywEj0CdyNhRjrGJSyOMV1-uSqNvNTE36G9DDs\",\"created_at\":\"2025-04-13T12:00:55.646\",\"updated_at\":\"2025-04-13T12:00:55.646\",\"status\":1,\"meta\":[{\"meta_id\":11,\"meta_key\":\"picture\",\"meta_value\":\"https://yt3.googleusercontent.com/HCv0fXFEEcD0HRyF0_qR1K7b7qO3KCzmIoyH1DEJYB94CIUFhIE5i2t2IDIPX97W1-DK4hegww=s160-c-k-c0x00ffffff-no-rj\"}]},\"post_title\":\"20250413131323_6oXpOQU5ms.ogg\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"20250413131323_6oXpOQU5ms.ogg\",\"post_mime_type\":\"audio/ogg;codecs=opus\",\"post_created_at\":\"2025-04-13T13:13:22.555184\",\"post_modified_at\":\"2025-04-13T13:13:22.547401\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":542,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131323_6oXpOQU5ms.ogg\"},{\"id\":543,\"key\":\"sar_file_name\",\"value\":\"20250413131323_6oXpOQU5ms.ogg\"},{\"id\":544,\"key\":\"sar_transcribed_text\",\"value\":\"비둔의주요정상은무엇인가요?\"},{\"id\":545,\"key\":\"sar_overall_sentiment\",\"value\":\"NEGATIVE\"},{\"id\":546,\"key\":\"sar_overall_score\",\"value\":\"0.65529716\"},{\"id\":547,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"}]},\"meta\":[{\"id\":542,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131323_6oXpOQU5ms.ogg\"},{\"id\":543,\"key\":\"sar_file_name\",\"value\":\"20250413131323_6oXpOQU5ms.ogg\"},{\"id\":544,\"key\":\"sar_transcribed_text\",\"value\":\"비둔의주요정상은무엇인가요?\"},{\"id\":545,\"key\":\"sar_overall_sentiment\",\"value\":\"NEGATIVE\"},{\"id\":546,\"key\":\"sar_overall_score\",\"value\":\"0.65529716\"},{\"id\":547,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"}]},{\"post\":{\"id\":215,\"author\":{\"id\":110,\"email\":\"admin@gmail.com\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-04-13\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiMTEwIiwiaWF0IjoxNzQ0NjAzOTc1LCJleHAiOjE3NDQ2MDQ4NzV9.fP9bEfywEj0CdyNhRjrGJSyOMV1-uSqNvNTE36G9DDs\",\"created_at\":\"2025-04-13T12:00:55.646\",\"updated_at\":\"2025-04-13T12:00:55.646\",\"status\":1,\"meta\":[{\"meta_id\":11,\"meta_key\":\"picture\",\"meta_value\":\"https://yt3.googleusercontent.com/HCv0fXFEEcD0HRyF0_qR1K7b7qO3KCzmIoyH1DEJYB94CIUFhIE5i2t2IDIPX97W1-DK4hegww=s160-c-k-c0x00ffffff-no-rj\"}]},\"post_title\":\"20250413131423_DdzV9GD2yL.ogg\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"20250413131423_DdzV9GD2yL.ogg\",\"post_mime_type\":\"audio/ogg;codecs=opus\",\"post_created_at\":\"2025-04-13T13:14:22.651803\",\"post_modified_at\":\"2025-04-13T13:14:22.64281\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":549,\"key\":\"sar_file_name\",\"value\":\"20250413131423_DdzV9GD2yL.ogg\"},{\"id\":550,\"key\":\"sar_transcribed_text\",\"value\":\"비듬의주요정상은무엇인가요?\"},{\"id\":551,\"key\":\"sar_overall_sentiment\",\"value\":\"NEGATIVE\"},{\"id\":552,\"key\":\"sar_overall_score\",\"value\":\"0.59957916\"},{\"id\":553,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"},{\"id\":548,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131423_DdzV9GD2yL.ogg\"}]},\"meta\":[{\"id\":549,\"key\":\"sar_file_name\",\"value\":\"20250413131423_DdzV9GD2yL.ogg\"},{\"id\":550,\"key\":\"sar_transcribed_text\",\"value\":\"비듬의주요정상은무엇인가요?\"},{\"id\":551,\"key\":\"sar_overall_sentiment\",\"value\":\"NEGATIVE\"},{\"id\":552,\"key\":\"sar_overall_score\",\"value\":\"0.59957916\"},{\"id\":553,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"},{\"id\":548,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131423_DdzV9GD2yL.ogg\"}]},{\"post\":{\"id\":216,\"author\":{\"id\":110,\"email\":\"admin@gmail.com\",\"role\":\"ADMINISTRATOR\",\"name\":\"관리자\",\"nickname\":\"admin\",\"birth\":\"2025-04-13\",\"gender\":\"F\",\"contact\":\"01000000000\",\"address\":\"서울시강남구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU5JU1RSQVRPUiIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiMTEwIiwiaWF0IjoxNzQ0NjAzOTc1LCJleHAiOjE3NDQ2MDQ4NzV9.fP9bEfywEj0CdyNhRjrGJSyOMV1-uSqNvNTE36G9DDs\",\"created_at\":\"2025-04-13T12:00:55.646\",\"updated_at\":\"2025-04-13T12:00:55.646\",\"status\":1,\"meta\":[{\"meta_id\":11,\"meta_key\":\"picture\",\"meta_value\":\"https://yt3.googleusercontent.com/HCv0fXFEEcD0HRyF0_qR1K7b7qO3KCzmIoyH1DEJYB94CIUFhIE5i2t2IDIPX97W1-DK4hegww=s160-c-k-c0x00ffffff-no-rj\"}]},\"post_title\":\"20250413131510_Be9f67SpeS.ogg\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"20250413131510_Be9f67SpeS.ogg\",\"post_mime_type\":\"audio/ogg;codecs=opus\",\"post_created_at\":\"2025-04-13T13:15:09.528023\",\"post_modified_at\":\"2025-04-13T13:15:09.519023\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":555,\"key\":\"sar_file_name\",\"value\":\"20250413131510_Be9f67SpeS.ogg\"},{\"id\":556,\"key\":\"sar_transcribed_text\",\"value\":\"지루성피부염의증상중부피의비름과가려움그리고각질이생기는것이일반적으로나타나는증상인가요?\"},{\"id\":557,\"key\":\"sar_overall_sentiment\",\"value\":\"POSITIVE\"},{\"id\":558,\"key\":\"sar_overall_score\",\"value\":\"0.6184943\"},{\"id\":559,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"},{\"id\":554,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131510_Be9f67SpeS.ogg\"}]},\"meta\":[{\"id\":555,\"key\":\"sar_file_name\",\"value\":\"20250413131510_Be9f67SpeS.ogg\"},{\"id\":556,\"key\":\"sar_transcribed_text\",\"value\":\"지루성피부염의증상중부피의비름과가려움그리고각질이생기는것이일반적으로나타나는증상인가요?\"},{\"id\":557,\"key\":\"sar_overall_sentiment\",\"value\":\"POSITIVE\"},{\"id\":558,\"key\":\"sar_overall_score\",\"value\":\"0.6184943\"},{\"id\":559,\"key\":\"sar_keyword_sentiment\",\"value\":\"{}\"},{\"id\":554,\"key\":\"url\",\"value\":\"/api/files/upload/110/20250413131510_Be9f67SpeS.ogg\"}]}]}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-음성분석결과 관련 글 메타 정보 조회 실패",
				description = "SUCCESS",
				content = @Content(	
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"회원을찾을수없습니다.\"}}"
					)
				) 
			)
	})
	@GetMapping("/sar")
	public ResponseEntity<?> getSarMetas(@Parameter(description = "로그인한 회원 토큰값") @RequestHeader("Authorization") String token){
		MemberDto findedMember = memberService.findMemberByToken(token);
		//회원 미존재시
		if(findedMember == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body( ResultUtil.fail( messageSource.getMessage("member.notfound", null, new Locale("ko")) ) );
		}
		
		List <SarResultDto> sarMetas = postMetaService.findAllSarMetas(findedMember.getId());
		
		return ResponseEntity.status(HttpStatus.OK).body( ResultUtil.success(sarMetas));
		
	}

}
