package com.tt.elephant.controller;

import com.tt.elephant.model.ResponseInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @PostMapping("/upload")
    public ResponseInfo uploadFile( @RequestParam("file") MultipartFile file){

        //TODO: upload file to S3
        return ResponseInfo.success("File uploaded successfully");
    }


    @PostMapping("/uploadMultiple")
    public ResponseInfo uploadMultipleFiles( @RequestParam("files") MultipartFile[] files){

        //TODO: upload multiple files to S3
        return ResponseInfo.success("Files uploaded successfully");

}
    }

