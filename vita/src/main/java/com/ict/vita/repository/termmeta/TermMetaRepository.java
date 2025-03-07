package com.ict.vita.repository.termmeta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.terms.TermsEntity;

@Repository
public interface TermMetaRepository extends JpaRepository<TermMetaEntity, Long> {

    
    List<TermMetaEntity> findAllByTermsEntity(TermsEntity termsEntity);


}
