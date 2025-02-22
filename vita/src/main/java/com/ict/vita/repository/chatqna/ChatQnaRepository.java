package com.ict.vita.repository.chatqna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatQnaRepository extends JpaRepository<ChatQnaEntity, Long> {

}
