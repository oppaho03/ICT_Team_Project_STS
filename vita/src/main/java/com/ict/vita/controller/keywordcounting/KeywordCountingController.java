package com.ict.vita.controller.keywordcounting;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ict.vita.service.keywordcounting.KeywordCountingRequestDto;
import com.ict.vita.service.keywordcounting.KeywordCountingResponseDto;
import com.ict.vita.service.keywordcounting.KeywordCountingService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termcategory.TermCategoryService;
import com.ict.vita.service.terms.TermsDto;
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
@CrossOrigin
@RequestMapping("/api/keywordcounting")
public class KeywordCountingController {

    private final KeywordCountingService keywordCountingService;
    private final TermCategoryService termCategoryService;
    
    private final MessageSource messageSource;

    /**
     * 키워드 카운팅 처리
     * @param id TermCategory ID
     * @return 키워드 카운팅 성공/실패 응답
     */
    @Operation(summary = "키워드 카운팅", description = "TermCategory ID로 해당 키워드 검색 시, count를 +1 또는 신규 저장합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
            schema = @Schema(implementation = String.class),
            examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":\"키워드 카운팅 저장 성공1312\"}}")
        )),
        @ApiResponse(responseCode = "404", description = "TermCategory 또는 Terms 정보 없음", content = @Content(
            examples = @ExampleObject(value = "{\"success\":0,\"response\":{\"message\":\"해당하는 값이 없음\"}}")
        ))
    })
    @GetMapping("/counting/{id}")
    public ResponseEntity<?> getKeywordCounting(
            @Parameter(description = "TermCategory ID") @PathVariable long id) {
        TermCategoryDto termCategoryDto = termCategoryService.findById(id);
        if (termCategoryDto == null || termCategoryDto.getTermsDto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResultUtil.fail(messageSource.getMessage("해당하는 키워드 없음",null, new Locale("ko"))));
        }
        keywordCountingService.saveOrUpdateKeywordCount(termCategoryDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultUtil.success(termCategoryDto));
    }

    /**
     * 특정 기간 내 키워드 검색 횟수 조회
     * @param requestDto 검색 키워드 정보, 시작일/종료일 포함
     * @return 키워드별 검색 수 목록
     */
    @Operation(summary = "키워드 검색 횟수 조회", description = "특정 기간 내 키워드별 검색 횟수를 조회합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
            schema = @Schema(implementation = KeywordCountingResponseDto.class),
            examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"keyword\":\"HIV\",\"count\":12}]}}")
        ))
    })
    @PostMapping("/search")
    public ResponseEntity<?> getKeywordStatus(@RequestBody KeywordCountingRequestDto requestDto) {
        List<KeywordCountingResponseDto> result = keywordCountingService
                .getCountBetweenDates(requestDto, requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(result));
    }

    /**
     * 특정 기간 내 키워드 랭킹 조회
     * @param requestDto 키워드 요청 정보 포함
     * @return 검색 빈도 기준 상위 5개 키워드
     */
    @Operation(summary = "키워드 랭킹 조회", description = "특정 기간 동안 가장 많이 검색된 키워드 Top 5를 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
            schema = @Schema(implementation = KeywordCountingResponseDto.class),
            examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"rank\":1,\"keyword\":\"AI\",\"count\":15}]}}")
        ))
    })
    @PostMapping("/ranking")
    public ResponseEntity<?> getRankCounting(@RequestBody KeywordCountingRequestDto requestDto) {
        List<KeywordCountingResponseDto> rank = keywordCountingService
                .getRankBetweenDates(requestDto, requestDto.getStartDate(), requestDto.getEndDate());
        return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(rank));
    }

    /**
     * 오늘 기준 실시간 키워드 랭킹 조회
     * @return 실시간 Top 10 키워드
     */
    @Operation(summary = "실시간 키워드 랭킹", description = "오늘 기준 실시간 Top 10 키워드를 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
            schema = @Schema(implementation = KeywordCountingResponseDto.class),
            examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"rank\":1,\"keyword\":\"의료\",\"count\":22}]}}")
        ))
    })
    @GetMapping("/realtime")
    public ResponseEntity<?> realTimeRanking() {
        List<KeywordCountingResponseDto> realTime = keywordCountingService.realTimeRank();
        return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(realTime));
    }

    /**
     * 월간 키워드 랭킹 조회 (이번 달)
     * @return 이번 달 키워드 Top 10
     */
    @Operation(summary = "월간 키워드 랭킹", description = "이번 달 1일부터 오늘까지의 키워드 Top 10을 반환합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "SUCCESS", content = @Content(
            schema = @Schema(implementation = KeywordCountingResponseDto.class),
            examples = @ExampleObject(value = "{\"success\":1,\"response\":{\"data\":[{\"rank\":1,\"keyword\":\"건강\",\"count\":19}]}}")
        ))
    })
    @GetMapping("/monthRanking")
    public ResponseEntity<?> getMonthRanking() {
        List<KeywordCountingResponseDto> monthRank = keywordCountingService.monthRanking();
        return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success(monthRank));
    }
}
