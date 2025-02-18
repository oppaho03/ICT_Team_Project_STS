package com.ict.vita.repository.termcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermCategoryRepository extends JpaRepository<TermCategoryEntity, Long>{

}
