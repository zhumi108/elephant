package com.tt.elephant.service;

import com.tt.elephant.model.ResponseInfo;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    ResponseInfo uploadFile(MultipartFile file);
}
