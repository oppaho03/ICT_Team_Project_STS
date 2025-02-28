package com.ict.vita.repository.membermeta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMetaRepository extends JpaRepository<MemberMetaEntity, Long> {

}
