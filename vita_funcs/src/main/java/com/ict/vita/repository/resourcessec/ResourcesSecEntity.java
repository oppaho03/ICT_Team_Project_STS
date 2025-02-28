package com.ict.vita.repository.resourcessec;

import org.hibernate.annotations.ColumnDefault;

import com.ict.vita.repository.posts.PostsEntity;
import com.ict.vita.repository.termcategory.TermCategoryEntity;
import com.ict.vita.repository.terms.TermsEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "APP_RESOURCES_SEC")
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//[리소스 보안]
public class ResourcesSecEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "APP_RESOURCES_SEC_SEQ")
	@SequenceGenerator(name = "APP_RESOURCES_SEC_SEQ",sequenceName = "APP_RESOURCES_SEC_SEQ",initialValue = 1,allocationSize = 1)
	@Column(columnDefinition = "NUMBER(20,0)")
	private Long id; //PK
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id",nullable = false) //테이블에서 FK명
	@NotNull
	private PostsEntity postsEntity; //글(포스트)
	
	@Column(columnDefinition = "NVARCHAR2(255)")
	private String file_name; //파일명(암호화)
	
	@Column(columnDefinition = "VARCHAR2(20)")
	@ColumnDefault("")
	private String file_ext = ""; //파일 확장자
	
	@Column(columnDefinition = "VARCHAR2(500)")
	@ColumnDefault("")
	private String file_url = ""; //파일URL(GUID)
	
	@Column(columnDefinition = "VARCHAR2(255)")
	@ColumnDefault("")
	private String enc_key = ""; //암호화 키
	
	@NotNull
	@Column(columnDefinition = "NUMBER(1)")
	@ColumnDefault("0")
	private long enc_status = 0; //암호화 상태(0: 암호화X)
}
