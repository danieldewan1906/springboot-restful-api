package com.learning.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadFileResponse {
    
    private String fileName;
    private String fileDownloadUrl;
    private String fileType;
    private long size;
}
