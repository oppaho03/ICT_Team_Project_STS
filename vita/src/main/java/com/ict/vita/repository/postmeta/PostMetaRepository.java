package com.ict.vita.repository.postmeta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostMetaRepository extends JpaRepository<PostMetaEntity, Long>{

}
