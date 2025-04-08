package com.ict.vita.controller.posts;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.util.UriEncoder;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberResponseDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.postmeta.PostMetaDto;
import com.ict.vita.service.postmeta.PostMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.posts.FileUploadDto;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsFileService;
import com.ict.vita.service.posts.PostsRequestDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.posts.PostsService;
import com.ict.vita.service.resourcessec.ResourcesSecDto;
import com.ict.vita.service.resourcessec.ResourcesSecService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.FileUtil;
import com.ict.vita.util.ResultUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin
public class PostsFileController {
	// 서비스 주입
	private final PostsService postsService;
    private final PostsFileService postsFileService;
    private final MemberService memberService;
    private final PostCategoryRelationshipsService pcrService;
    private final TermsService termService;
    private final PostMetaService postMetaService;
    private final ResourcesSecService resourcesSecService;
    private final MemberMetaService memberMetaService;
    
    private final MessageSource messageSource;
    
    @Value("${file.upload-dir}") //application.yml에 설정
    private String uploadDir; //업로드 디렉터리
  
    /**
     * [파일 업로드]
     * @param token 회원 토큰값
     * @param fileInfo 파일
     * @return
     */
    @Operation( summary = "파일 업로드", description = "파일 업로드 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "201-파일 업로드 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = PostsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":{\"id\":97,\"author\":{\"id\":102,\"email\":\"wowwow@naver.com\",\"name\":\"우수정\",\"nickname\":\"와우\",\"birth\":\"2020-03-26\",\"gender\":\"F\",\"contact\":null,\"address\":\"서울시서초구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoid293d293QG5hdmVyLmNvbSIsInN1YiI6IjEwMiIsImlhdCI6MTc0MzczNjExOSwiZXhwIjoxNzQzNzM3MDE5fQ.PvyzKJQWBcAjwg5yDli2Sy44K3RMyCO36btZ5mLK-h8\",\"created_at\":\"2025-04-04T12:06:00.070694\",\"updated_at\":\"2025-04-04T12:09:51.541382\",\"status\":1,\"meta\":[]},\"post_title\":\"바다.jpg\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"%EB%B0%94%EB%8B%A4.jpg\",\"post_mime_type\":\"image/jpeg\",\"post_created_at\":\"2025-04-07T19:03:59.1278669\",\"post_modified_at\":\"2025-04-07T19:03:59.1278669\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":2,\"key\":\"url\",\"value\":\"/api/files/upload/102/바다.jpg\"}]}}}"
				)
			) 
		),
		@ApiResponse( 
				responseCode = "400-파일 업로드 실패",
				description = "FAIL", 
				content = @Content(					
					examples = @ExampleObject(
						value = "{\"success\":0,\"response\":{\"message\":\"파일업로드에실패했습니다.\"}}"
					)
				) 
			),
		@ApiResponse( 
			responseCode = "401-파일 업로드 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
    @PostMapping("/files/upload")
    public ResponseEntity<?> uploadFile(
    		@RequestHeader(name = Commons.AUTHORIZATION) String token,
    		FileUploadDto fileInfo) { //가능한 경우1
//    		MultipartFile file) { //가능한 경우2
//    		@RequestPart(value = "file",required = false) MultipartFile file) { //가능한 경우3
//    		@RequestParam(value = "file",required = false) MultipartFile file) { //가능한 경우4
    	/* [파일업로드시 메서드 인자]
    	 * 파일업로드시 메서드 인자로 MultipartFile이 포함된 DTO객체로 받을 경우,
    	    객체 앞에 @RequestPart 나 @RequestParam 같은 어노테이션 붙이면 못받음!
    	 * 파일업로드시 메서드 인자로 MultipartFile로 받을 경우,
    	    MultipartFile 앞에 @RequestPart 나 @RequestParam 같은 어노테이션 붙일 수 있다.
    	*/
    	
    	//토큰으로 회원 조회
    	MemberDto loginMember = memberService.findMemberByToken(token); 
    	//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		} 
		
		//파일 미첨부시
		if(fileInfo.getFile() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResultUtil.fail( messageSource.getMessage("file.upload.fail", null, new Locale("ko")) ));
		}
		
		//* 파일 업로드 저장 위치를 C:/upload-dir/계정아이디 폴더가 생성되고 거기다가 각 회원의 파일이 저장되게 하고싶어서 아래 추가해봄
		
		//업로드 디렉터리 경로에 회원id 추가
		String memberUploadDir = uploadDir + String.valueOf(loginMember.getId());
		
		//업로드 디렉터리가 없으면 디렉터리 생성
        Path uploadDirectory = Paths.get(memberUploadDir);
        if (!Files.exists(uploadDirectory)) {
            try {
            	System.out.println("===== 디렉터리 생성 =====");
            	uploadDirectory = Files.createDirectories(uploadDirectory);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[파일 업로드]업로드 디렉터리 생성 실패");
			}
        }       
        
        //새 파일명
        String newFileName = FileUtil.getNewFileName(uploadDirectory.toString(), fileInfo.getFile().getOriginalFilename());
        System.out.println("새 파일명:"+newFileName);
        
        //글 객체 생성
		PostsDto post = PostsDto.builder()
							.memberDto(loginMember)
							.post_title( newFileName )
							.post_content(null)
							.post_summary(null)
							.post_status(Commons.POST_STATUS_PUBLISH)
							.post_pass( null )
							.post_name( UriEncoder.encode( newFileName ) )
							.post_mime_type( fileInfo.getFile().getContentType() )
							.post_created_at(LocalDateTime.now())
							.post_modified_at(LocalDateTime.now())
							.comment_status(Commons.COMMENT_STATUS_CLOSE)
							.comment_count(0)
							.build();  
							
		//1.파일을 APP_POSTS 테이블에 저장
		PostsDto savedPost = postsService.savePost(post);
        
        //2.파일을 글-카테고리 관계 테이블에 저장 (카테고리명: media)
        TermCategoryDto category = termService.findBySlugByCategory("media", "media"); 
        pcrService.save(savedPost, List.of(category.getId()));
        
        //3.글 메타 테이블에 저장 
        String fileUrl = "/api/files/upload/" + String.valueOf(loginMember.getId()) + "/" + newFileName;
        ObjectMetaRequestDto postMeta = ObjectMetaRequestDto.builder()
								        .id(savedPost.getId())
								        .meta_key("url") //파일 경로 저장
								        .meta_value( fileUrl ) 
								        .build();
        System.out.println("파일 url:" + fileUrl );
        PostMetaDto savedPostMeta = postMetaService.save(postMeta);
        
        //4.파일을 APP_RESOURCES_SEC 테이블에 저장
        String ext = newFileName.substring( newFileName.lastIndexOf(".") + 1 );
        
        ResourcesSecDto resourcesSecDto = ResourcesSecDto.builder()
									        .postsDto(savedPost)
									        .file_name(EncryptAES256.encrypt(newFileName))
									        .file_ext( ext )
									        .file_url(fileUrl) 
									        .enc_key(null)
									        .enc_status(1) //암호화o
									        .build();
        resourcesSecService.save(resourcesSecDto);
    	
        //5.File 업로드 : 파일 업로드는 C드라이브의 upload-dir 폴더 안의 회원id폴더 안에 파일이 업로드됨
        //File객체 생성
  		File f = new File(uploadDirectory + File.separator + newFileName);
  		try {
  			fileInfo.getFile().transferTo(f); //파일 업로드
  		} catch (Exception e) {
  			System.out.println("File 업로드 실패!");
  			e.printStackTrace();
  		} 
        
        //프론트로 PostsResponseDto 반환
  		List<TermsResponseDto> categories = List.of(TermsResponseDto.toDto(category));
  		
  		List<PostMetaResponseDto> postMetas = postMetaService.findAll(savedPost)
									  				.stream()
									  				.map(dto -> PostMetaResponseDto.toResponseDto(dto))
									  				.toList();
  		
  		List <MemberMetaResponseDto> memberMetas =  memberMetaService.findAll(loginMember)
									  				.stream()
									  				.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
									  				.toList();
  		
  		PostsResponseDto postResponse = PostsResponseDto.toDto(savedPost.toEntity(), categories, postMetas, memberMetas);
  		
  		return ResponseEntity.status(HttpStatus.CREATED).body(ResultUtil.success( postResponse ));

    }
    
    /**
     * [음성파일 월별 검색] - 관리자는 전체 검색, 일반회원은 본인것만 검색 가능
     * @param token 회원 토큰값
     * @param period 기간 ex."2025-04"
     * @return
     */
    @Operation( summary = "음성파일 월별 검색", description = "음성파일 월별 검색 API" )
	@ApiResponses({
		@ApiResponse( 
			responseCode = "200-음성파일 월별 검색 성공",
			description = "SUCCESS",
			content = @Content(	
				schema = @Schema(implementation = PostsResponseDto.class),
				examples = @ExampleObject(
					value = "{\"success\":1,\"response\":{\"data\":[{\"id\":105,\"author\":{\"id\":102,\"email\":\"wowwow@naver.com\",\"role\":\"USER\",\"name\":\"우수정\",\"nickname\":\"와우\",\"birth\":\"2020-03-26\",\"gender\":\"F\",\"contact\":null,\"address\":\"서울시서초구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoid293d293QG5hdmVyLmNvbSIsInN1YiI6IjEwMiIsImlhdCI6MTc0MzczNjExOSwiZXhwIjoxNzQzNzM3MDE5fQ.PvyzKJQWBcAjwg5yDli2Sy44K3RMyCO36btZ5mLK-h8\",\"created_at\":\"2025-04-04T12:06:00.070694\",\"updated_at\":\"2025-04-04T12:09:51.541382\",\"status\":1,\"meta\":[]},\"post_title\":\"5e2ac6f85807b852d9e01ffe.wav\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"5e2ac6f85807b852d9e01ffe.wav\",\"post_mime_type\":\"audio/wave\",\"post_created_at\":\"2025-04-08T11:13:23.050945\",\"post_modified_at\":\"2025-04-08T11:13:23.042935\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":10,\"key\":\"url\",\"value\":\"/api/files/upload/102/5e2ac6f85807b852d9e01ffe.wav\"}]},{\"id\":104,\"author\":{\"id\":102,\"email\":\"wowwow@naver.com\",\"role\":\"USER\",\"name\":\"우수정\",\"nickname\":\"와우\",\"birth\":\"2020-03-26\",\"gender\":\"F\",\"contact\":null,\"address\":\"서울시서초구\",\"token\":\"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImVtYWlsIjoid293d293QG5hdmVyLmNvbSIsInN1YiI6IjEwMiIsImlhdCI6MTc0MzczNjExOSwiZXhwIjoxNzQzNzM3MDE5fQ.PvyzKJQWBcAjwg5yDli2Sy44K3RMyCO36btZ5mLK-h8\",\"created_at\":\"2025-04-04T12:06:00.070694\",\"updated_at\":\"2025-04-04T12:09:51.541382\",\"status\":1,\"meta\":[]},\"post_title\":\"5e2ac52c5807b852d9e01fe4.wav\",\"post_content\":null,\"post_summary\":null,\"post_status\":\"PUBLISH\",\"post_pass\":null,\"post_name\":\"5e2ac52c5807b852d9e01fe4.wav\",\"post_mime_type\":\"audio/wave\",\"post_created_at\":\"2025-04-08T11:11:58.316716\",\"post_modified_at\":\"2025-04-08T11:11:58.277709\",\"comment_status\":\"CLOSE\",\"comment_count\":0,\"categories\":[{\"id\":818,\"name\":\"미디어\",\"slug\":\"media\",\"group_number\":0,\"category\":\"media\",\"description\":null,\"count\":0,\"parent\":0}],\"meta\":[{\"id\":9,\"key\":\"url\",\"value\":\"/api/files/upload/102/5e2ac52c5807b852d9e01fe4.wav\"}]}]}}"
				)
			) 
		),
		@ApiResponse( 
			responseCode = "401-음성파일 월별 검색 실패",
			description = "FAIL", 
			content = @Content(					
				examples = @ExampleObject(
					value = "{\"success\":0,\"response\":{\"message\":\"접근권한이없습니다.\"}}"
				)
			) 
		)
	})
    @GetMapping("/voice-files")
    public ResponseEntity<?> getMonthlyVoiceFiles(
    		@RequestHeader(name = Commons.AUTHORIZATION) String token,
    		@RequestParam("period") String period){
    	//토큰으로 회원 조회
    	MemberDto loginMember = memberService.findMemberByToken(token); 
    	//회원이 존재하지 않는 경우
		if(loginMember == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResultUtil.fail( messageSource.getMessage("user.invalid_token", null, new Locale("ko")) ));
		} 
		
		//검색할 미디어 타입
		String type = "audio";
		//월별 음성 파일 검색
		List<PostsDto> monthlyFiles = postsFileService.getMonthlyVoiceFiles(period, type);
		
		List<PostsResponseDto> result = null;
		
		result = monthlyFiles
				.stream()
				.map( postDto -> {
					PostsEntity entity = postDto.toEntity();
					
					TermCategoryDto category = termService.findBySlugByCategory("media", "media"); 
					List<TermsResponseDto> categories = List.of(TermsResponseDto.toDto(category));
					
					List<PostMetaResponseDto> postMeta = postMetaService.findAll(postDto)
															.stream()
															.map(meta -> PostMetaResponseDto.toResponseDto(meta))
															.toList();
					
					List <MemberMetaResponseDto> memberMeta = memberMetaService.findAll(MemberDto.toDto(entity.getMemberEntity()))
																.stream()
																.map(meta -> MemberMetaResponseDto.toResponseDto(meta))
																.toList();
					
					//관리자가 아니면서 본인 글이 아닌 경우
					if(!loginMember.getRole().equals(Commons.ROLE_ADMINISTRATOR) 
							&& postDto.getMemberDto().getId() != loginMember.getId())
						return null;
					
					return PostsResponseDto.toDto(entity, categories, postMeta, memberMeta);
				})
				.toList();
		
		result = result
				.stream()
				.filter(Objects::nonNull) // null 제거
				.toList();
		
		return ResponseEntity.status(HttpStatus.OK).body(ResultUtil.success( result ));
		
    }

}


