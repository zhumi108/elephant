package com.tt.elephant.controller;

import com.tt.elephant.jwt.JwtToken;
import com.tt.elephant.model.ResponseInfo;
import com.tt.elephant.service.UploadFileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Tag(name = "文件上传接口", description = "File Upload API")
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    private UploadFileService fileService;

    @JwtToken
    @PostMapping("/upload")
    public ResponseInfo uploadFile( @RequestParam("file") MultipartFile file){
        return fileService.uploadFile(file);
    }

//    @PostMapping("/uploadMultiple")
//    public ResponseInfo uploadMultipleFiles( @RequestParam("files") MultipartFile[] files){
//
//        //TODO: upload multiple files to S3
//        return ResponseInfo.success("Files uploaded successfully");
//    }
}

