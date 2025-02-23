package com.ict.vita.repository.postcategoryrelationships;

import jakarta.persistence.Embeddable;

//[APP_POST_CATEGORY_RELATIONSHIPS 테이블의 복합키]
@Embeddable
public class PostCategory {
	private Long post_id; //글(포스트) id
	private Long term_category_id; //카테고리 id
}
