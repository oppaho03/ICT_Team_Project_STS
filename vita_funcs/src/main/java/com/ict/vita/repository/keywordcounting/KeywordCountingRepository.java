package com.ict.vita.repository.keywordcounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordCountingRepository extends JpaRepository<KeywordCountingEntity, Long> {

}
