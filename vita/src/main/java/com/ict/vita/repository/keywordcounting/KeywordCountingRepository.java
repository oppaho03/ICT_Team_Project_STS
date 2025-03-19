package com.ict.vita.repository.keywordcounting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

@Repository
public interface KeywordCountingRepository extends JpaRepository<KeywordCountingEntity, Long> {

	KeywordCountingEntity findByTermsEntityAndSearchedAt(TermsEntity entity, String serachedDate);


}
