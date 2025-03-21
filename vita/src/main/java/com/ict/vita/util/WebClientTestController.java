package com.ict.vita.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.reactive.function.client.WebClient;

//import reactor.core.publisher.Mono;

@RestController
//[WebClient 테스트용]
public class WebClientTestController {

	//WebClient.builder().baseUrl("baseUrl값").build();
	//private WebClient webClient = WebClient.builder().baseUrl("https://jsonplaceholder.typicode.com").build();
	//private WebClient webClient = WebClient.builder().build();

	@PostMapping("/wc")
	public ResponseEntity<?> wcTest(){
		//파이썬으로 요청
		System.out.println("wcTest");
		//"https://jsonplaceholder.typicode.com/posts"

		Map<String, Object> map = new HashMap<>();
		map.put("userId", 10);
		map.put("id", 2);
		map.put("title", "타이틀");
		map.put("body", "바디");
		
//		sendDataToPython(map);
		
		//Mono<String> response = sendDataToPython(map);
		
//		System.out.println("response:"+response);
		
		//리액트로 리다이렉트
//		  HttpHeaders headers = new HttpHeaders();
//        headers.add("Location", "리액트 redirect url");
//        return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	/*
	@Async // 비동기 실행
	//CompletableFuture<T>는 자바에서 "비동기 작업"을 수행하고 그 결과를 나중에 받을 수 있도록 지원하는 객체
    public CompletableFuture<Void> sendDataToPython(Map<String, Object> map) {
		
		webClient.post()
				//.uri("/posts") //엔드포인트
				.uri("https://jsonplaceholder.typicode.com/posts") //엔드포인트
				.header("Content-Type", "application/json")
				.bodyValue(map) //요청의 바디에 넣어줄것을 설정
				.retrieve() //응답을 가져오기 위한 호출
				.bodyToMono(Void.class) // 응답을 무시(비동기 실행)
				.subscribe(); // 비동기 처리

        return CompletableFuture.completedFuture(null); //비동기 작업이 끝난 후 값을 반환(비동기 작업이 끝났음을 즉시 알리는 역할) 			
    } */
	
//	public Mono<String> sendDataToPython(Map<String, Object> map) {
//		System.out.println("sendDataToPython");
//        return webClient.post()
//                .uri("https://jsonplaceholder.typicode.com/posts") // 요청할 엔드포인트
//                .header("Content-Type", "application/json") // JSON 데이터 전송
//                .bodyValue(map) // 요청 바디에 JSON 데이터 추가
//                .retrieve() // 응답을 가져오기 위한 호출
//                .bodyToMono(String.class); // 응답바디의 데이터를 String 형태로 변환
//                //.block(); // 동기 처리: 응답이 올 때까지 기다림
//    }
	
	
	@GetMapping("/wc")
	public ResponseEntity<?> getTest(){
		System.out.println("getTest");
		//String result = sendDataToPython2();
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
//	public Mono<String> sendDataToPython2() {
//		System.out.println("sendDataToPython2");
//        return webClient.get()
//                .uri("https://jsonplaceholder.typicode.com/posts") // 요청할 엔드포인트
//                .retrieve() // 응답을 가져오기 위한 호출
//                .bodyToMono(String.class); // 응답바디의 데이터를 String 형태로 변환
//                //.block(); // 동기 처리: 응답이 올 때까지 기다림
//    }
//	public String sendDataToPython2() {
//		System.out.println("sendDataToPython2");
//        String result = webClient.get()
//                .uri("https://jsonplaceholder.typicode.com/posts") // 요청할 엔드포인트
//                .retrieve() // 응답을 가져오기 위한 호출
//                .bodyToMono(String.class) // 응답바디의 데이터를 String 형태로 변환
//                .block(); // 동기 처리: 응답이 올 때까지 기다림
//        System.out.println("result:"+result);
//        return result;
//    }
	
}
