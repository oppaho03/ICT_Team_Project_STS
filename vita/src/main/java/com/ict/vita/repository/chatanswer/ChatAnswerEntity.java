package com.ict.vita.repository.chatanswer;

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
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_CHAT_ANSWER")
	@SequenceGenerator(name = "APP_CHAT_ANSWER",sequenceName = "APP_CHAT_ANSWER",initialValue = 1,allocationSize = 1)
	private long id; //PK 
	
	@NotNull
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
