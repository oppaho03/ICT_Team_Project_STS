package com.ict.vita.repository.postcategoryrelationships;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

//[APP_POST_CATEGORY_RELATIONSHIPS 테이블의 복합키]
@Getter
@Setter
@Embeddable
public class PostCategory {
	private Long post_id; //글(포스트) id
	private Long term_category_id; //카테고리 id
}
