package com.ict.vita.repository.posts;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.util.Commons;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_POSTS")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[글(포스트)]
public class PostsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_POSTS_SEQ")
	@SequenceGenerator(name = "APP_POSTS_SEQ",sequenceName = "APP_POSTS_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@JoinColumn(name = "post_author",nullable = false) //테이블에서 FK명
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private MemberEntity memberEntity; //글 작성자
	
	@Lob
	@NotNull
	@Column(name = "post_title")
	private String postTitle; //글 제목
	
	@Lob
	@Column(columnDefinition = "CLOB DEFAULT EMPTY_CLOB()",name = "post_content")
	private String postContent; //글 내용
	
	@Lob
	@Column(columnDefinition = "CLOB DEFAULT EMPTY_CLOB()",name = "post_summary")
	private String postSummary; //글 요약
	
	@Column(columnDefinition = "VARCHAR2(20)",name = "post_status")
	@NotNull
	@ColumnDefault("PUBLISH")
	private String postStatus = Commons.POST_STATUS_PUBLISH; //글 상태(공개/비공개/삭제)
	
	@Column(columnDefinition = "VARCHAR2(255)",name = "post_pass")
	@ColumnDefault("")
	private String postPass = ""; //글 비밀번호
	
	@Column(columnDefinition = "VARCHAR2(200)",name = "post_name")
	@ColumnDefault("")
	private String postName = ""; //글 이름(별칭)
	
	@Column(columnDefinition = "VARCHAR2(100)",name = "post_mime_type")
	@ColumnDefault("")
	private String postMimeType = ""; //글 타입(글/미디어파일)
	
	@NotNull
	@Column(columnDefinition = "TIMESTAMP",name = "post_created_at")
	@ColumnDefault("SYSDATE")
	@CreationTimestamp
	private LocalDateTime postCreatedAt; //글 생성일
	
	@NotNull
	@Column(columnDefinition = "TIMESTAMP",name = "post_modified_at")
	private LocalDateTime postModifiedAt; //글 수정일
	
	@NotNull
	@Column(columnDefinition = "VARCHAR2(20)",name = "comment_status")
	@ColumnDefault("OPEN")
	private String commentStatus = Commons.COMMENT_STATUS_OPEN; //댓글 허용 상태
	
	@Column(columnDefinition = "NUMBER(20)",name = "comment_count")
	@ColumnDefault("0")
	private long commentCount = 0; //댓글 개수
}
