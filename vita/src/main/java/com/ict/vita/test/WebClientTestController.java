package com.ict.vita.test;

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

import com.ict.vita.util.ResultUtil;

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
		String result = response.block(); // block(): 동기 처리 | subscribe(): 비동기 처리
		
		//리액트로 리다이렉트
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "http://192.168.0.55:8080/where");

        return ResponseEntity.status(HttpStatus.FOUND).body(ResultUtil.fail("http://192.168.0.55:8080/where"));
        // return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302 Redirect

	}
	
	public Mono<String> sendDataToPython(Map<String, Object> map) {
		/* [HJH 정리] - 참고: https://thalals.tistory.com/381
		 << WebFlux 모듈 >>
		 - client, server 에서 reactive 스타일의 어플리케이션의 개발을 도와주는 스프링 모듈
		 - 요청을 처리하는 방식이 비동기 논블러킹 방식
		 - 리액터 라이브러리(Reactor library)와 넷티(Netty)를 기반으로 동작함
		 - 리액티브 프로그래밍 : 데이터 스트림을 이용하여 데이터를 전달하는 비동기 프로그래밍
		 - 리액터는 리액티브 스트림을 구현하는 라이브러리로 Mono 와 Flux 2가지 데이터 타입으로 스트림을 정의
		 - WebFlux 에서는 모든 응답을 Mono 혹은 Flux 에 담아서 반환해주어야 함!!
		 
		 < Mono >
		 - 비동기적으로 데이터를 처리하기 위한 객체다.
		 - 0-1개의 결과만을 처리하기 위한 Reactor의 객체
		 - 0 또는 하나의 데이터 항목과 에러를 가짐
		 
		 < Flux >
		 - 0-N개인 여러 개의 결과를 처리하는 객체
		 - 0 또는 하나 이상의 데이터 항목과 에러를 가짐
		 
		*/
		
		/* <테스트용>
		Mono<ResponseEntity<Void>> response = webClient.post() //post요청
		        .uri("/posts") 
		        .header("Content-Type", "application/json") 
		        .bodyValue(map) 
		        .retrieve()
		        .toBodilessEntity(); //응답바디 필요없을때
		System.out.println("[sendDataToPython]status:"+response.block().getStatusCode());
		System.out.println("[sendDataToPython]headers:"+response.block().getHeaders());
		System.out.println("[sendDataToPython]body:"+response.block().getBody());
		*/
		
		
        return webClient.post() //post요청
                .uri("/posts") // 요청할 엔드포인트
                .header("Content-Type", "application/json") // JSON 데이터 전송
                .bodyValue(map) // 요청 바디에 JSON 데이터 추가
                .retrieve() // 응답을 가져오기 위한 호출
                .bodyToMono(String.class); // 응답바디의 데이터를 String 형태로 변환
                //.block(); // 동기 처리: 응답이 올 때까지 기다림  <->  subscribe() : 비동기 처리
        
        
        /*
         - bodyToMono()는 응답바디의 값을 가져올때 사용
         
         < 응답바디가 필요없는 경우> - 3가지 방법
         - releaseBody()
         - toBodilessEntity(): responseBody는 없으나, Http StatusCode와 Headers를 가진 ResponseEntity
         - bodyToMono(Void.class)
        */
    }
	
}
