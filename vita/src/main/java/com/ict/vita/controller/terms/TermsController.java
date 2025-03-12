package com.ict.vita.controller.terms;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsRequestDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.JwtUtil;
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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/terms")
@CrossOrigin
//[용어 Controller]
public class TermsController {
	//서비스 주입
	private final MessageSource messageSource;

	private final TermsService termsService;	
	private final MemberService memberService;
	
	private final JwtUtil jwtutil; // Constructor Injection, JwtUtil
	

	/**
	 * 모두 검색 
	 * @return TermDto 배열 반환
	 */	
	@CrossOrigin
	@Operation( summary = "모두 검색 ", description = "용어(Term) 리스트 반환" )
	@ApiResponses({
		@ApiResponse(
			responseCode = "200",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = TermsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":709,\"term_id\":681,\"name\":\"감염성질환\",\"slug\":\"%EA%B0%90%EC%97%BC%EC%84%B1%EC%A7%88%ED%99%98\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0}]}}"
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
	@GetMapping
	public ResponseEntity<?> getAll( 
		@Parameter(description = "페이지") @RequestParam(required = false, defaultValue = "0") int p, 
		@Parameter(description = "출력 개수 제한") @RequestParam(required = false, defaultValue = "50") int ol ){
		// (@RequestHeader(value = "Authorization", required = true) String authHeader)
	
		List<TermCategoryDto> termCategoryDtoList = new ArrayList<>();

		if ( p > 0 ) termCategoryDtoList.addAll(termsService.findAll(p, ol));
		else termCategoryDtoList.addAll(termsService.findAll());
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termCategoryDtoList.stream().map(TermsResponseDto::toDto).collect(Collectors.toList()) ));

	} /// findAll()
	
	/**
	 * 이름 검색 
	 * @return TermDto 배열 반환
	 */	
	@CrossOrigin
	@Operation( summary = "이름 검색 ", description = "이름으로 검색색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":714,\"term_id\":686,\"name\":\"구순염\",\"slug\":\"cheilitis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681}]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/byname/{name}")
	public ResponseEntity<?> getAllByName(@Parameter(description = "이름") @PathVariable String name ) { return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termsService.findAllByName( name ).stream().map(TermsResponseDto::toDto).collect(Collectors.toList()) )); }

	/**
	 * 카테고리(Taxonomy) 검색
	 * @param taxonomy 카테고리명
	 * @return TermDto 배열 반환
	 */	
	@CrossOrigin
	@Operation( summary = "카테고리(Taxonomy) 검색", description = "카테고리(Taxonomy) 검색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":714,\"term_id\":686,\"name\":\"구순염\",\"slug\":\"cheilitis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":681}]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/category/{taxonomy}")
	public ResponseEntity<?> getAllByCategory( @Parameter(description = "카테고리(Taxonomy)") @PathVariable String taxonomy ) { return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termsService.findAllByTaxonomy( taxonomy ).stream().map(TermsResponseDto::toDto).collect(Collectors.toList()) )); }

	/**
	 * ID 검색 
	 * @return TermsResponseDto 또는 Null 반환
	 */	
	@CrossOrigin
	@Operation( summary = "ID 검색 ", description = "ID 검색" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"id\":1288,\"term_id\":1260,\"name\":\"전이성신장암\",\"slug\":\"%EC%A0%84%EC%9D%B4%EC%84%B1%20%EC%8B%A0%EC%9E%A5%EC%95%94\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":1179}}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":null}}")))
	})
	@GetMapping("/{id}") 
	public ResponseEntity<?> getById(@Parameter(description = "카테고리 ID") @PathVariable Long id ) {

		TermCategoryDto termCategoryDto = termsService.findById( id );

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termCategoryDto == null ? null : TermsResponseDto.toDto(termCategoryDto) )); 
	}

	/**
	 * 검색 : 슬러그 또는 부모 TermCategory ID
	 * @param slug 
	 * @param category
	 * @return TermsResponseDto 또는 Null 또는 List 반환
	 */	
	@CrossOrigin
	@GetMapping("/s")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"id\":1466,\"term_id\":1438,\"name\":\"정신의학과\",\"slug\":\"%EC%A0%95%EC%8B%A0%EC%9D%98%ED%95%99%EA%B3%BC\",\"group_number\":0,\"category\":\"department\",\"description\":null,\"count\":0,\"parent\":0}}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":1404,\"term_id\":1376,\"name\":\"객혈\",\"slug\":\"%EA%B0%9D%ED%98%88\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":1375},{\"id\":1405,\"term_id\":1377,\"name\":\"공기가슴증(기흉)\",\"slug\":\"pneumothorax\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":1375},{\"id\":1406,\"term_id\":1378,\"name\":\"규폐증\",\"slug\":\"%EA%B7%9C%ED%8F%90%EC%A6%9D\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":1375},{\"id\":1407,\"term_id\":1379,\"name\":\"급성기관지염\",\"slug\":\"acute_bronchitis\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":1375}]}}"))),
		@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	})
	@Operation( summary = "검색 ", description = "검색 (카테고리명(Taxonomy) 포함)" )
	public ResponseEntity<?> getBySlug( 
		@Parameter(description = "슬러그") @RequestParam(required = false, defaultValue = "") String slug, 
		@Parameter(description = "카테고리(Taxonomy)") @RequestParam(required = false, defaultValue = "") String category,
		@Parameter(description = "부모 TermCategory ID") @RequestParam(required = false, defaultValue = "-1") Long parent
	) { 

		if ( ! Commons.isNull(slug) && ! Commons.isNull(category) ) {
			// << 검색 1. 슬러그 검색 >>
			try {
				String dslug = URLDecoder.decode(slug.trim(), "UTF-8");
				slug = URLEncoder.encode(dslug, "UTF-8");

				TermCategoryDto termCategoryDto = termsService.findBySlugByCategory( slug, category );
				
				return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termCategoryDto == null ? null : TermsResponseDto.toDto(termCategoryDto) )); 
			}
			catch ( Exception e ) {
				System.out.println( e.getMessage() );
			}
		}
		else if ( parent >= 0 ) {
			// << 검색 2. 부모 카테고리 Id >>
			// - 카테고리 ID -> Term ID 로 변환
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termsService.findByParent( parent ) )); 
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail(  "Invalid values..." )); 
	}

	/**
	 * 새 Term, TermCategory 등록
	 * @param dto FindTermDto
	 * @return TermsResponseDto 또는 오류 메시지 반환
	 */
	@CrossOrigin
	@Operation( summary = "새 Term, TermCategory 등록", description = "새 Term, TermCategory 등록" )
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"id\":1537,\"term_id\":1511,\"name\":\"테스트질병\",\"slug\":\"%ED%85%8C%EC%8A%A4%ED%8A%B8+%EC%A7%88%EB%B3%91\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0}}}"))),
		@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	})
	@PostMapping("/")
	public ResponseEntity<?> add( 
		@RequestHeader(value = "Authorization", required = true) String token,
		@Parameter( description = "데이터") @RequestBody @Valid TermsRequestDto dto, 		
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
		String content = Commons.formatBindingResultHasError(bindingResult, "용어(Term) 유효성 검증 실패");
		if ( content != null ) System.out.println( content );

		// < Term 등록 >
		try {

			// .parent, .count 값 초기화 
			if ( dto.getGroup_number() < 0L ) dto.setGroup_number( 0L );
			if ( dto.getParent() < 0L ) dto.setParent( 0L );
			if ( dto.getParent() < 0L ) dto.setCount( 0L );
			
			// - 이름 유효성 검사
			if ( Commons.isNull(dto.getName()) ) throw new Exception( String.format("이름 없는 (슬러그 : %s) 용어를 등록할 수 없습니다.", dto.getSlug() ) );

			// - 카테고리명 (taxonomy) 값 파싱 
			String taxonomy = Commons.isNull(dto.getCategory()) ? "category" : dto.getCategory();

			// - 슬러그 유효성 검사 
			// - 슬러그 값이 없을 경우 이름으로 대체
			if ( Commons.isNull(dto.getSlug()) ) {
				// slug 값 입력되지 않은 경우, 이름을 URLEncode 하여 사용
				dto.setSlug( dto.getName() ); 
			}

			// - 슬러그 URL Encoding
			String slug = dto.getSlug();
			slug = URLDecoder.decode(slug, "UTF-8"); // 기본 (original)
			dto.setSlug( URLEncoder.encode(slug, "UTF-8") ); // URLEncode 

			TermCategoryDto result = termsService.save(dto);
			if ( result == null ) {
				// - 슬러그 중복 (등록 중단)
				throw new Exception(
					String.format(
						"%s (슬러그 : %s) 용어를 등록할 수 없습니다.", 
						dto.getName(), 
						dto.getSlug()
					)
				);
			} // - 슬러그 중복 검사 (종료)
			
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( TermsResponseDto.toDto(result) ));
		}
		catch (Exception e) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResultUtil.fail(e.getMessage()));
		}
	} /// add

	/**
	 * Term, TermCategory 변경
	 * @param dto FindTermDto
	 * @return TermsResponseDto 또는 오류 메시지 반환
	 */
	@CrossOrigin
	@Operation(summary = "Term, TermCategory 변경", description = "Term, TermCategory 변경")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = TermsResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":{\"id\":1537,\"term_id\":1511,\"name\":\"테스트질병\",\"slug\":\"%ED%85%8C%EC%8A%A4%ED%8A%B8+%EC%A7%88%EB%B3%91\",\"group_number\":0,\"category\":\"disease\",\"description\":null,\"count\":0,\"parent\":0}}}"))),
		@ApiResponse(responseCode = "400", description = "ERROR", content = @Content(examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"Invalid values...\"}}")))
	})
	@PutMapping("/edit")
	public ResponseEntity<?> edit( 
		@RequestHeader(value = "Authorization", required = true) String token,
		@Parameter( description = "데이터") @RequestBody @Valid TermsRequestDto dto, 
		BindingResult bindingResult ) {

		// < JWT Token 유효성 검사 >
		MemberDto user = Commons.findMemberByToken(token, memberService);
		if ( user == null ) {
			return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		}

		if ( dto.getTerm_id() == null ) 
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( "용어를 수정할 수 없습니다. TermCategory ID is Null." ));
		
		Object result = termsService.update(dto);

		if ( result instanceof TermCategoryDto ) {
			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( TermsResponseDto.toDto((TermCategoryDto)result) ));
		}
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( "용어를 수정할 수 없습니다." ));

		
	}
}

