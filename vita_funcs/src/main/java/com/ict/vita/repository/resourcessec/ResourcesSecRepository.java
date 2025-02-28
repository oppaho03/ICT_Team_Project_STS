package com.ict.vita.repository.resourcessec;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourcesSecRepository extends JpaRepository<ResourcesSecEntity, Long> {

}
