package com.ict.vita.repository.membersnslogins;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSnsLoginsRepository extends JpaRepository<MemberSnsLoginsEntity, Long> {

}
