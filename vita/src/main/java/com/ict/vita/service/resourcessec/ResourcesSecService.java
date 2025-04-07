package com.ict.vita.service.resourcessec;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ict.vita.repository.resourcessec.ResourcesSecEntity;
import com.ict.vita.repository.resourcessec.ResourcesSecRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesSecService {
	//리포지토리 주입
	private final ResourcesSecRepository resourcesSecRepository;
	
	/**
	 * [저장]
	 * @param dto ResourcesSecDto객체
	 * @return
	 */
	public ResourcesSecDto save(ResourcesSecDto dto) {
		ResourcesSecEntity entity = dto != null ? dto.toEntity() : null;
		ResourcesSecEntity savedEntity = resourcesSecRepository.save(entity);
		return ResourcesSecDto.toDto(savedEntity);
	}
}
