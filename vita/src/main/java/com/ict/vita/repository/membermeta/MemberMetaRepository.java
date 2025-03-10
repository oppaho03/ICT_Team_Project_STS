package com.ict.vita.repository.membermeta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.repository.terms.TermsEntity;

@Repository
public interface MemberMetaRepository extends JpaRepository<MemberMetaEntity, Long> {

    List<MemberMetaEntity> findAllByMemberEntity(MemberEntity termsEntity);

    MemberMetaEntity findByMemberEntityAndMetaKey(MemberEntity memberEntity, String metakey);

}
