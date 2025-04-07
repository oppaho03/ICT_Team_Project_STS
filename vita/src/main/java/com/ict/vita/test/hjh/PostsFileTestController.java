package com.ict.vita.test.hjh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

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

import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.member.MemberService;
import com.ict.vita.service.membermeta.MemberMetaResponseDto;
import com.ict.vita.service.membermeta.MemberMetaService;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.postcategoryrelationships.PostCategoryRelationshipsService;
import com.ict.vita.service.postmeta.PostMetaResponseDto;
import com.ict.vita.service.postmeta.PostMetaService;
import com.ict.vita.service.posts.PostsDto;
import com.ict.vita.service.posts.PostsRequestDto;
import com.ict.vita.service.posts.PostsResponseDto;
import com.ict.vita.service.resourcessec.ResourcesSecDto;
import com.ict.vita.service.resourcessec.ResourcesSecService;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.terms.TermsResponseDto;
import com.ict.vita.service.terms.TermsService;
import com.ict.vita.util.Commons;
import com.ict.vita.util.EncryptAES256;
import com.ict.vita.util.FileUtil;
import com.ict.vita.util.ResultUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
@CrossOrigin
public class PostsFileTestController {
	 // 서비스 주입
    private final PostsFileTestService postsFileService;
    private final MemberService memberService;
    private final PostCategoryRelationshipsService pcrService;
    private final TermsService termService;
    private final PostMetaService postMetaService;
    private final ResourcesSecService resourcesSecService;
    private final MemberMetaService memberMetaService;
    
    private final MessageSource messageSource;
    
    @Value("${file.upload-dir}") //application.yml에 설정
    private String uploadDir; //업로드 디렉터리
  
    @PostMapping("/upload")
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

		System.out.println("[파일업로드]");
		System.out.println("파일명:"+fileInfo.getFile().getOriginalFilename());
		System.out.println("파일타입:"+fileInfo.getFile().getContentType());
		
		//***** 파일 업로드 저장 위치를 C:/upload-dir/계정아이디 폴더가 생성되고 거기다가 각 회원의 파일이 저장되게 하고싶어서 아래 추가해봄
		
		//업로드 디렉터리 경로에 회원id 추가
		String memberUploadDir = uploadDir + String.valueOf(loginMember.getId());
		
		// 업로드 디렉터리가 없으면 디렉터리 생성
        Path uploadDirectory = Paths.get(memberUploadDir);
        if (!Files.exists(uploadDirectory)) {
            try {
            	uploadDirectory = Files.createDirectories(uploadDirectory);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[파일 업로드]업로드 디렉터리 생성 실패");
			}
        } 
        
        //새 파일명
        String newFileName = FileUtil.getNewFileName(uploadDirectory.toString(), fileInfo.getFile().getOriginalFilename());
        
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
        PostsDto savedPost = postsFileService.savePostFile(post); 
        
        //2.파일을 글-카테고리 관계 테이블에 저장 (카테고리명: media)
        TermCategoryDto category = termService.findBySlugByCategory("media", "media"); 
        pcrService.save(post, List.of(category.getId()));
        
        //3.글 메타 테이블에 저장 
        ObjectMetaRequestDto postMeta = ObjectMetaRequestDto.builder()
								        .id(post.getId())
								        .meta_key("url") //파일 경로 저장
								        .meta_value( uploadDirectory.toUri().toString() ) //*************
								        .build();
        postMetaService.save(postMeta);
        
        //4.파일을 APP_RESOURCES_SEC 테이블에 저장
        ResourcesSecDto resourcesSecDto = ResourcesSecDto.builder()
									        .postsDto(savedPost)
									        .file_name(EncryptAES256.encrypt(newFileName))
									        .file_ext( fileInfo.getFile().getContentType() )
									        .file_url(null) //**********??
									        .enc_key(null)
									        .enc_status(1) //암호화o
									        .build();
        
        resourcesSecService.save(resourcesSecDto);
    	
        //5.File 업로드
        //File객체 생성
  		File f = new File(uploadDirectory + File.separator + newFileName);
  		//업로드
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
    

}


