package com.ict.vita.repository.chatanswer;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_CHAT_ANSWER")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatAnswerEntity {
	/* 
	 Long의 최대 자릿수가 19자리라서 NUMBER(19,0)로 자동 매핑됨
	  --> NUMBER(20,0)을 명확하게 설정하려면 @Column의 columnDefinition에 명시해야 함
	*/
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_CHAT_ANSWER_SEQ")
	@SequenceGenerator(name = "APP_CHAT_ANSWER_SEQ",sequenceName = "APP_CHAT_ANSWER_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK 
	
	@NotNull
	@Column(length = 20)
	private String file_name; //파일이름
	
	@Lob
	@NotNull
	private String intro; //인트로
	
	@Lob
	@NotNull
	private String body; //메인내용
	
	@Lob
	@NotNull
	private String conclusion; //결론
}
