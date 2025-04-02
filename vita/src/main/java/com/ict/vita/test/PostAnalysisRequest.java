package com.ict.vita.test;

import lombok.Data;

@Data
public class PostAnalysisRequest {
	private Long postId;
    private String sttResult;
    private double sentimentScore;
}
