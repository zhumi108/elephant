package com.tt.elephant.service.impl;

import com.tt.elephant.exception.CustomException;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.model.UploadFileRspVO;
import com.tt.elephant.service.UploadFileService;
import com.tt.elephant.util.MinioUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    @Autowired
    private MinioUtil minioUtil;

    @Override
    public ResponseInfo uploadFile(MultipartFile file) {
        try {
            // 上传文件
            String url = minioUtil.uploadFile(file);

            // 构建成功返参，将图片的访问链接返回
            ResponseInfo responseInfo = new ResponseInfo(200, "upload succeed");
            responseInfo.setData(UploadFileRspVO.builder().url(url).build());
            return responseInfo;
        } catch (Exception e) {
            log.error("==> 上传文件至 Minio 错误: ", e);
            // 手动抛出业务异常，提示 “文件上传失败”
            throw new CustomException("文件上传失败");
        }
    }
}
