package com.rz.lease.web.admin.controller.apartment;

import com.rz.lease.common.result.Result;
import com.rz.lease.web.admin.service.FileService;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File management")
@RequestMapping("/admin/file")
@RestController
public class FileUploadController {

    private FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Upload file")
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Result<String> upload(@RequestPart("file") MultipartFile file) throws InvalidKeyException,
            ErrorResponseException, InsufficientDataException, InternalException, InvalidResponseException,
            NoSuchAlgorithmException, ServerException, XmlParserException, IllegalArgumentException, IOException {
        // later switch to frontend upload

        String url = fileService.upload(file);
        return Result.ok(url);

    }

}
