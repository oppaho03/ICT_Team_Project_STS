package com.ict.vita.repository.termmeta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermMetaRepository extends JpaRepository<TermMetaEntity, Long> {

}
