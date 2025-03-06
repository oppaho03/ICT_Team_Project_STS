package com.ict.vita.repository.anc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AncRepository extends JpaRepository<AncEntity, AnswerCategory> {


    @Query(value="""
        SELECT * FROM APP_ANC m
		WHERE m.answer_id = :answer_id 
		""", 
        nativeQuery = true )
    List<AncEntity> findByAnswerId( @Param("answer_id") Long answer_id );

    @Query(value="""
        SELECT * FROM APP_ANC m
		WHERE m.term_category_id = :term_category_id 
		""", 
        nativeQuery = true )
    List<AncEntity> findAllByTermCategoryId( @Param("term_category_id") Long term_category_id );

}
