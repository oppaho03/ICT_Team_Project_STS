package com.ict.vita.repository.chatanswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatAnswerRepository extends JpaRepository<ChatAnswerEntity, Long>{

}
