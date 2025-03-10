package com.ict.vita.repository.membermeta;

import com.ict.vita.repository.member.MemberEntity;
import com.ict.vita.repository.postmeta.PostMetaEntity;
import com.ict.vita.repository.posts.PostsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_MEMBER_META")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[회원 메타]
public class MemberMetaEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_MEMBER_META_SEQ")
	@SequenceGenerator(name = "APP_MEMBER_META_SEQ",sequenceName = "APP_MEMBER_META_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)",name = "meta_id")
	private Long metaId; //PK
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id",nullable = false) //테이블에서 FK명
	@NotNull
	private MemberEntity memberEntity;
	
	@NotNull
	@Column( name="meta_key", columnDefinition = "VARCHAR2(255)")
	private String metaKey; //메타 키
	
	@Lob
	@Column( name = "meta_value", columnDefinition = "CLOB DEFAULT EMPTY_CLOB()")
	private String metaValue; //메타 값
}
