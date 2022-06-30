package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.requests.UploadType;
import com.studyforces.sourcesapi.responses.FileURLResponse;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import com.studyforces.sourcesapi.services.FileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/file")
@PreAuthorize("hasRole('editor')")
public class FileController {

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    private final FileService fileService;

    @PostMapping
    FileUploadResponse request(@RequestHeader("Content-Type") String contentType,
                               @RequestParam(name = "type", defaultValue = "SOURCE") UploadType type) throws Exception {
        String fileName = UUID.randomUUID().toString();

        switch (type) {
            case SOURCE -> fileName = "sources/" + fileName;
            case ATTACHMENT -> fileName = "attachments/" + fileName;
        }

        FileUploadResponse res = new FileUploadResponse();

        res.setFileName(fileName);
        res.setUrl(fileService.uploadURL(fileName, contentType));

        return res;
    }

    @GetMapping
    public FileURLResponse view(@RequestParam("file") String fileName) throws Exception {
        return FileURLResponse.builder().url(fileService.objectURL(fileName)).build();
    }

}
