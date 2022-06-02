package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.services.FileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@PreAuthorize("hasRole('editor')")
public class UploadController {
    private final FileService fileService;

    public UploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/upload")
    Map<String, String> getPresignedUpload(@RequestHeader("Content-Type") String contentType) throws Exception {
        HashMap<String, String> res = new HashMap<>();
        res.put("url", fileService.uploadURL(contentType));

        return res;
    }
}
