package com.ict.vita.service.posts;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SarMonthlyDto {
	private Long postId; //글id
	private String yearMonth; //ex) 2025-04
	private Long audioPostCount;
	
	private List<SpeechAnalysisResultDto> sarResult;
}
