package com.ict.vita.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ict.vita.repository.resourcessec.ResourcesSecEntity;

@Repository
public interface ResourcesSecRepository2 extends JpaRepository<ResourcesSecEntity, Long>{

}
