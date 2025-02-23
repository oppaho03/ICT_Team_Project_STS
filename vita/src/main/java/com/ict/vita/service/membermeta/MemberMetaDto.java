package com.ict.vita.service.membermeta;

import com.ict.vita.repository.membermeta.MemberMetaEntity;
import com.ict.vita.service.member.MemberDto;
import com.ict.vita.service.postmeta.PostMetaDto;
import com.ict.vita.service.posts.PostsDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[회원 메타 DTO]
public class MemberMetaDto {
	private Long meta_id; //PK
	private MemberDto memberDto;
	private String meta_key; //메타 키
	private String meta_value; //메타 값
	
	//[MemberMetaDto를 MemberMetaEntity로 변환하는 메서드]
	public MemberMetaEntity toEntity() {
		return MemberMetaEntity.builder()
				.meta_id(meta_id)
				.memberEntity(memberDto.toEntity())
				.meta_key(meta_key)
				.meta_value(meta_value)
				.build();
	}
	
	//[MemberMetaEntity를 MemberMetaDto로 변환하는 메서드]
	public static MemberMetaDto toDto(MemberMetaEntity entity) {
		return MemberMetaDto.builder()
				.meta_id(entity.getMeta_id())
				.memberDto(MemberDto.toDto(entity.getMemberEntity()))
				.meta_key(entity.getMeta_key())
				.meta_value(entity.getMeta_value())
				.build();
	}
}
