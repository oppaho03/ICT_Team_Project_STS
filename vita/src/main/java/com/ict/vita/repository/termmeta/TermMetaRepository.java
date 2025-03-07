package com.ict.vita.repository.termmeta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.terms.TermsEntity;
import com.ict.vita.service.terms.TermsDto;

@Repository
public interface TermMetaRepository extends JpaRepository<TermMetaEntity, Long> {

    
    List<TermMetaEntity> findAllByTermsEntity(TermsEntity termsEntity);

    List<TermMetaEntity> findAllByTermsEntityAndMetaKey(TermsEntity termsEntity, String metakey);
    TermMetaEntity findByTermsEntityAndMetaKey(TermsEntity termsEntity, String metakey);



}
