package com.ict.vita.repository.externalquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalQuestionRepository extends JpaRepository<ExternalQuestionEntity, Long> {

}
