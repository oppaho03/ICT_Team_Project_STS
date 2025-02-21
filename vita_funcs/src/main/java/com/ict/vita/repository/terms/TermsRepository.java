package com.ict.vita.repository.terms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//[용어 Repository]
public interface TermsRepository extends JpaRepository<TermsEntity, Long>{

}
