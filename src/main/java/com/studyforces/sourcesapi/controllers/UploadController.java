package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.exceptions.UploadControllerException;
import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.requests.SaveSourceRequest;
import com.studyforces.sourcesapi.services.FileService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@PreAuthorize("hasRole('editor')")
public class UploadController {
    private final FileService fileService;
    private final SourceUploadRepository sourceUploadRepository;

    public UploadController(FileService fileService, SourceUploadRepository sourceUploadRepository) {
        this.fileService = fileService;
        this.sourceUploadRepository = sourceUploadRepository;
    }

    @PostMapping("/request")
    Map<String, String> requestUploadURL(@RequestHeader("Content-Type") String contentType) throws Exception {
        HashMap<String, String> res = new HashMap<>();
        String fileName = UUID.randomUUID().toString();
        res.put("fileName", fileName);
        res.put("url", fileService.uploadURL(fileName, contentType));

        return res;
    }

    @PostMapping("/save")
    SourceUpload saveSource(@RequestBody SaveSourceRequest req) throws Exception {
        if (!fileService.fileExists(req.getFileName())) {
            throw new UploadControllerException("File doesn't exist");
        }

        SourceUpload upload = new SourceUpload();
        upload.setSourceFile(req.getFileName());

        return sourceUploadRepository.save(upload);
    }
}
