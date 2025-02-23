package com.ict.vita.repository.anc;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AncRepository extends JpaRepository<AncEntity, AnswerCategory> {

}
