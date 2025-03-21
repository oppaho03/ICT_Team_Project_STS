package com.ict.vita.repository.terms;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.OneToOne;

@Repository
//[용어 Repository]
public interface TermsRepository extends JpaRepository<TermsEntity, Long>{
	
	List<TermsEntity> findAll( Sort sort );
	Page findAll( Pageable pageable );
	List<TermsEntity> findAllByName(String name, Sort sort); 
	
}
