package com.ict.vita.service.securedfile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SecuredFileDTO {
    private Long postId;
    private String fileName;
    private String fileExt;
    private String fileUrl;
    private Integer encStatus;

    public SecuredFileDTO(Long postId, String fileName, String fileExt, String fileUrl, Integer encStatus) {
        this.postId = postId;
        this.fileName = fileName;
        this.fileExt = fileExt;
        this.fileUrl = fileUrl;
        this.encStatus = encStatus;
    }
}
