package com.ict.vita.controller.membermeta;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.others.ObjectMetaResponseDto;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member/meta")
@CrossOrigin
public class MemberMetaController {
	//서비스 주입
	private final MessageSource messageSource;
	private final MemberService memberService;

	private final MemberMetaService memberMetaService;	

	/**
	 * 검색
	 * @param meta_id 메타 아이디
	 * @param meta_key 메타 키
	 * @param id 오브젝트 ID
	 * @return ObjectMetaResponseDto | List<ObjectMetaResponseDto> | Null
	 */	
	@Operation( summary = "전체 검색", description = "전체 검색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testvalue\"}}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":null}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ObjectMetaResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"meta_id\":1,\"meta_key\":\"_test\",\"meta_value\":\"testmetavalue\"}]}}"))),
		@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	})
	@GetMapping("/")
	public ResponseEntity<?> getAll (
		@Parameter(description = "메타 ID", required = false ) @RequestParam(defaultValue = "0") Long meta_id,		
		@Parameter(description = "메타 키", required = false) @RequestParam(defaultValue = "") String meta_key,
		@Parameter(description = "오브젝트 ID", required = false) @RequestParam(defaultValue = "0") Long id
	) {

		// < 검색 조건 별 : 메타 정보 반환 >
		if ( meta_id != 0 ) {
			MemberMetaDto metaDto = memberMetaService.findById(meta_id); 
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( metaDto == null ? null : ObjectMetaResponseDto.toDto(metaDto.toEntity())  ));
		}
		else if ( id != 0 ) {
			
			// < 검색 조건 : 오브젝트 ID > 

			MemberDto oDto = memberService.findMemberById(id);
			if ( oDto == null ) {
				// 오브젝트가 존재하지 않기 때문에 검색 실패
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( Commons.i18nMessages(messageSource, "member.notfound") )); 
			}

			if ( Commons.isNull(meta_key) ) { // - 메타 키 값이 없음, 전체 리스트 반환
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( memberMetaService.findAll(oDto).stream().map( dto->ObjectMetaResponseDto.toDto(dto.toEntity()) ).toList() ) );
			}
			else { // - 메타 키 값으로 검색
				
				// meta_key 유효성 검사 
				MemberMetaDto metaDto = memberMetaService.findByMemberDtoByMetaKey( MemberMetaDto.builder().memberDto(oDto).meta_key(meta_key).build() );

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

		// Term ID 유효성 검사
		MemberDto member = memberService.findMemberById(id);
		if ( member != null ) {

			dto.setMeta_value(meta_value); // 메타 값 업데이트

			MemberMetaDto result = memberMetaService.save( dto );
			if ( result != null ) 
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( ObjectMetaResponseDto.toDto(result.toEntity()) ));
		}
		else errmsg = Commons.i18nMessages(messageSource, "member.notfound");

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail(errmsg  ));	

	}

}
