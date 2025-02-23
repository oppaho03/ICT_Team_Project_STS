package com.ict.vita.repository.postcategoryrelationships;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCategoryRelationshipsRepository extends JpaRepository<PostCategoryRelationshipsEntity, PostCategory> {

}
