package com.ict.vita.repository.securedfile;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.springframework.boot.autoconfigure.domain.EntityScan;

@Entity
@Table(name = "APP_RESOURCES_SEC")
@Getter
@Setter
@NoArgsConstructor
public class SecuredFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "FILE_NAME", nullable = false, length = 255)
    private String fileName;

    @Column(name = "FILE_EXT", nullable = false, length = 20)
    private String fileExt;

    @Column(name = "FILE_URL", length = 500)
    private String fileUrl;

    @Column(name = "ENC_KEY", length = 255)
    private String encKey;

    @Column(name = "ENC_STATUS", nullable = false)
    private Integer encStatus;

  
    // NULL 방지: postId setter 추가
    public void setPostId(Long postId) {
        this.postId = (postId != null) ? postId : 1L;
        System.out.println("[엔티티] postId 설정됨: " + this.postId);
    }

    // Hibernate 저장 직전 postId NULL 방지
    @PrePersist
    public void prePersist() {
        if (this.postId == null) {
            this.postId = 1L;
            System.out.println("[엔티티] PrePersist - postId 기본값 설정됨: " + this.postId);
        }
    }
}