package com.ict.vita.repository.membersnslogins;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberSnsLoginsRepository extends JpaRepository<MemberSnsLoginsEntity, Long> {

	@Query(value = """
			select s.*
			from APP_MEMBER_SNS_LOGINS s
			join APP_MEMBER m on s.member_id = m.id
			where m.email = :email
			""", nativeQuery = true)
	Optional<MemberSnsLoginsEntity> findByEmail(@Param("email") String email);

}
