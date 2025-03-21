package com.ict.vita.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

//import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
//[WebClient 테스트용]
public class WebClientTestController {

	private WebClient webClient = WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();
	//private WebClient webClient = WebClient.builder().build();

	@PostMapping("/wc")
	public ResponseEntity<?> wcTest(){
		//파이썬으로 요청
		System.out.println("wcTest1");
		//"https://jsonplaceholder.typicode.com/posts"

		Map<String, Object> map = new HashMap<>();
		map.put("userId", 10);
		map.put("id", 2);
		map.put("title", "타이틀");
		map.put("body", "바디");

		Mono<String> response = sendDataToPython(map);
		String result = response.block(); //동기 처리
		
		//리액트로 리다이렉트
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://192.168.0.55:8080/where");

        return ResponseEntity.status(HttpStatus.FOUND).body(ResultUtil.fail("http://192.168.0.55:8080/where"));
        // return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect

	}
	
	public Mono<String> sendDataToPython(Map<String, Object> map) {
        return webClient.post()
                .uri("/posts") // 요청할 엔드포인트
                .header("Content-Type", "application/json") // JSON 데이터 전송
                .bodyValue(map) // 요청 바디에 JSON 데이터 추가
                .retrieve() // 응답을 가져오기 위한 호출
                .bodyToMono(String.class); // 응답바디의 데이터를 String 형태로 변환
                //.block(); // 동기 처리: 응답이 올 때까지 기다림  <->  subscribe() : 비동기 처리
    }
	
}
