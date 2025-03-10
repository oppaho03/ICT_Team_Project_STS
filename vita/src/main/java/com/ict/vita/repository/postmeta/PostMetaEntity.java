package com.ict.vita.repository.postmeta;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.repository.terms.TermsEntity;

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

@Table(name = "APP_POST_META")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[글(포스트) 메타]
public class PostMetaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_POST_META_SEQ")
	@SequenceGenerator(name = "APP_POST_META_SEQ",sequenceName = "APP_POST_META_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)",name = "meta_id")
	private Long metaId; //PK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id",nullable = false) //테이블에서 FK명
	@NotNull
	private PostsEntity postsEntity;
	
	@NotNull
	@Column(name = "meta_key", columnDefinition = "VARCHAR2(255)")
	private String metaKey; //메타 키
	
	@Lob
	@Column(name = "meta_value", columnDefinition = "CLOB DEFAULT EMPTY_CLOB()")
	private String metaValue; //메타 값
}
