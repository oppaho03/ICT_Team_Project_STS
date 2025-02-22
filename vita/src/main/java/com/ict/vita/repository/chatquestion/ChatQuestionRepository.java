package com.ict.vita.repository.chatquestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatQuestionRepository extends JpaRepository<ChatQuestionEntity, Long> {

}
