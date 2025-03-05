package com.ict.vita.repository.postcategoryrelationships;

import com.ict.vita.repository.anc.AncEntity;
import com.ict.vita.repository.anc.AnswerCategory;
import com.ict.vita.repository.chatanswer.ChatAnswerEntity;
import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.termcategory.TermCategoryEntity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_POST_CATEGORY_RELATIONSHIPS")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[글 카테고리(관계)]
public class PostCategoryRelationshipsEntity {
	@EmbeddedId //복합키 구현
	private PostCategory id; //PK(FK2개로 이루어진 복합키)
	
	@MapsId("post_id") //해당 FK가 복합키(@EmbeddedId)의 어떤 변수인지를 설정
	@JoinColumn(name = "post_id") //DB에서 외래키명 설정
	@ManyToOne(fetch = FetchType.LAZY)
	private PostsEntity postsEntity; //글(포스트)
	
	@MapsId("term_category_id") //해당 FK가 복합키(@EmbeddedId)의 어떤 변수인지를 설정
	@JoinColumn(name = "term_category_id") //DB에서 외래키명 설정
	@ManyToOne(fetch = FetchType.LAZY)
	private TermCategoryEntity termCategoryEntity; //카테고리

}
