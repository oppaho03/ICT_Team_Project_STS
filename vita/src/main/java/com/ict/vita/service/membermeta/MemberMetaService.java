package com.ict.vita.service.membermeta;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.member.MemberRepository;
import com.ict.vita.repository.membermeta.MemberMetaEntity;
import com.ict.vita.repository.membermeta.MemberMetaRepository;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.termmeta.TermMetaEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.others.ObjectMetaRequestDto;
import com.ict.vita.service.termcategory.TermCategoryDto;
import com.ict.vita.service.termmeta.TermMetaDto;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberMetaService {
	//리포지토리 주입
	private final MemberMetaRepository memberMetaRepository;
	private final MemberRepository memberRepository;

	/**
	 * 전체 검색 (회원 ID)
	 * @param reqDto MemberDto
	 * @return 메타 리스트 반환
	 */	
	public List<MemberMetaDto> findAll( MemberDto reqDto ) {

		List<MemberMetaEntity> memberMetaEntities = new ArrayList<>();
		Optional<MemberEntity> member = memberRepository.findById(reqDto.getId());
		
		if ( member.isPresent() )
			memberMetaEntities.addAll( memberMetaRepository.findAllByMemberEntity( member.get() ) );
			
		return memberMetaEntities.stream().map( entity->MemberMetaDto.toDto(entity) ).toList();
	}


	/**
	 * TermsDto AND 메타 키 검색
	 * @param metaDto TermsDto
	 * @param meta_key 메타 키
	 * @return 메타 또는 NULL 반환
	 */	
	public MemberMetaDto findByMemberDtoByMetaKey ( MemberMetaDto metaDto ) {

		// < TermMetaDto 로 검사 > 
		if ( metaDto.getMemberDto() == null ) return null;

		MemberMetaEntity result = memberMetaRepository.findByMemberEntityAndMetaKey( metaDto.getMemberDto().toEntity(), metaDto.getMeta_key());

		return result == null ? null : MemberMetaDto.toDto(result);
	}

	/**
	 * ID 검색
	 * @param id 메타 ID
	 * @return 메타 또는 NULL 반환
	 */	
	public MemberMetaDto findById( Long id ) {
		MemberMetaEntity metaEntity = memberMetaRepository.findById(id).orElse(null);
		return metaEntity == null ? null : MemberMetaDto.toDto(metaEntity);
	}
	
	/**
	 * 등록  
	 * @param reqDto ObjectMetaRequestDto
	 * @return 메타 값 반환
	 */	
	public MemberMetaDto save( ObjectMetaRequestDto reqDto ) {
		// < 회원 ID - 카테고리 엔티티 검색 >
		MemberEntity memberEntity = memberRepository.findById(reqDto.getId()).orElse(null);
		
		if ( memberEntity != null ) {
			// 메타 엔티티 
			MemberMetaEntity metaEntity = new MemberMetaEntity();
			metaEntity.setMemberEntity( memberEntity );
			metaEntity.setMetaKey( reqDto.getMeta_key() );
			metaEntity.setMetaValue( reqDto.getMeta_value() );

			/// < 현재 MemberEntity 와 MetaKey 값으로 중복 확인 >			
			MemberMetaDto resDto = findByMemberDtoByMetaKey( MemberMetaDto.toDto(metaEntity) );
			if ( resDto != null ) 
				metaEntity.setMetaId(resDto.getMeta_id());  // 해당 메타 데이터 존재할 경우 덮어쓰기

			metaEntity = memberMetaRepository.save( metaEntity );
			return metaEntity == null ? null : MemberMetaDto.toDto(metaEntity);
		}
		else return null;
	}
	
	
}
