package com.ict.vita.controller.anc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.vita.service.anc.AncDto;
import com.ict.vita.service.anc.AncService;
import com.ict.vita.service.chatanswer.ChatAnswerDto;
import com.ict.vita.service.chatanswer.ChatAnswerResponseDto;
import com.ict.vita.service.chatanswer.ChatAnswerService;
import com.ict.vita.service.others.ObjectCategoryRelDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsResponseDto;
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
@RequestMapping("/api/answercategories")
@CrossOrigin
public class AncController {
	//서비스 주입
	private final AncService ancService;
	private final ChatAnswerService answerService;
	private final TermCategoryService categoryService;

	/**
	 * 카테고리 목록 가져오기
	 * @param id 답변 ID
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
	@GetMapping("/answer/{id}")
	public ResponseEntity<?> findCategories(@Parameter(description = "답변 ID", example = "1") @PathVariable Long id) {

		// 대상 ID 로 관계 검색 
		List<AncDto> relDtos = ancService.findAllByAnswerId(id);

		
		if ( relDtos != null && ! relDtos.isEmpty() ) {
			
			
			List<TermsResponseDto> termCatsDto = relDtos.stream().map( dto -> TermsResponseDto.toDto(dto.getTermCategoryDto().toEntity()) ).toList();

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( termCatsDto ));
		}

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}

	
	/**
	 * 답변 목록 가져오기
	 * @param id 카테고리 ID
	 * @return
	 */	
	@Operation(summary = "답변 목록 가져오기", description = "답변 목록 가져오기")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(schema = @Schema(implementation = ChatAnswerResponseDto.class), examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"id\":290495,\"file_name\":\"HC-A-02770676000153\",\"intro\":\"HIV감염을예방하기위한몇가지중요한요소가있습니다.\",\"body\":\"첫째로,HIV감염자와성적접촉은피해야합니다.HIV감염자와의신체적접촉은면역체계를약화시킬수있으므로,모든성적활동에대해서는안전을보장하기위해적절한예방조치를취해야합니다.둘째로,주사도구등을공유하지않는것이중요합니다.주사도구를공유하는것은HIV감염의위험을높일수있습니다.\",\"conclusion\":\"HIV감염예방을위해백신접종과콘돔사용은반드시권장되는사항입니다.또한,HIV감염우려가있는파트너와의성관계도주의해야합니다.감염된파트너는치료를통해정상적인면역체계를회복할수있으므로,이러한위험을피하기위해예방적인조치를취해야합니다.\"},{\"id\":290496,\"file_name\":\"HC-A-02771275000154\",\"intro\":\"HIV(인간면역결핍바이러스)감염을예방하는것은감염위험을줄이고안전한성생활을유지하기위한필수적인조치입니다.\",\"body\":\"HIV에감염된사람은건강한생활방식과사회적관계를유지함으로써예방할수있습니다.정기적인성교육과콘돔사용,감염위험에노출된사람과의성접촉을피하는등의예방조치를통해감염위험을최소화할수있습니다.\",\"conclusion\":\"HIV감염의예방은모든사람에게매우중요한문제입니다.개인의안전과사회적관계의유지는HIV감염위험을줄이고안전한성생활을유지하는데필수적입니다.\"},]}}"))),
		@ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[]}}")))
	})
	@GetMapping("/category/{id}")
	public ResponseEntity<?> findAnswers(@Parameter(description = "카테고리 ID") @PathVariable Long id) {

		// 대상 ID 로 관계 검색 
		List<AncDto> relDtos = ancService.findAllByTermCategoryId(id);

		if ( relDtos != null && ! relDtos.isEmpty() ) {
			//카테고리 찾기
			TermCategoryDto findedCategory = categoryService.findById(id);
			
			List<ChatAnswerResponseDto> answerDtos = relDtos.stream().map( rel -> ChatAnswerResponseDto.toDto( rel.getChatAnswerDto().toEntity(),List.of(TermsResponseDto.toDto(findedCategory)) ) ).toList();

			return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( answerDtos ));
		}

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( new ArrayList() ));
	}

	/**
	 * 카테고리 등록 
	 * @param reldto ObjectCategoryRelDto ( id, categories )
	 * @return
	 */		
	@Operation(summary = "카테고리 등록", description = "카테고리 등록")	
	@PostMapping("/")
	public ResponseEntity<?> addCategories(@Parameter( description = "관계 데이터") @RequestBody ObjectCategoryRelDto reldto ) {
		/* CHECKE AUTH *** */
		Long id = reldto.getId();
		List<Long> categories = reldto.getCategories();

		if ( id == null || id == 0 || categories == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( id == 0 ? "포스트 ID 값으로 NULL 또는 0 을 사용할 수 없습니다." : "카테고리 목록으로 NULL 을 사용할 수 없습니다."  ));
		}
		// < 답변 ID 유효성 검사 >
		ChatAnswerDto answerDto = answerService.findById(id);
		if ( answerDto == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( "유효하지 않은 답변 ID 입니다." ));
		}

		// < 답변 ID 로 카테고리 목록 비교 및 추가 / 삭제 >
		List<AncDto> relDtos = ancService.update(answerDto, categories);

		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( relDtos.stream().map(dto->TermsResponseDto.toDto(dto.getTermCategoryDto().toEntity())).toList() ));
	}

}
