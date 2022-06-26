package com.studyforces.sourcesapi.controllers;

import com.studyforces.sourcesapi.exceptions.UploadControllerException;
import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.requests.SaveSourceRequest;
import com.studyforces.sourcesapi.requests.UploadType;
import com.studyforces.sourcesapi.responses.FileInfoResponse;
import com.studyforces.sourcesapi.responses.FileURLResponse;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import com.studyforces.sourcesapi.services.FileService;
import com.studyforces.sourcesapi.services.SourceService;
import io.minio.StatObjectResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
@PreAuthorize("hasRole('editor')")
public class UploadController {
    private final FileService fileService;
    private final SourceService sourceService;
    private final SourceUploadRepository sourceUploadRepository;

    public UploadController(FileService fileService, SourceUploadRepository sourceUploadRepository, SourceService sourceService) {
        this.fileService = fileService;
        this.sourceUploadRepository = sourceUploadRepository;
        this.sourceService = sourceService;
    }

    @PostMapping("/request")
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

    @PostMapping("/save")
    SourceUpload saveSource(@RequestBody SaveSourceRequest req) throws Exception {
        if (!fileService.fileExists(req.getFileName())) {
            throw new UploadControllerException("File doesn't exist");
        }

        SourceUpload upload = new SourceUpload();
        upload.setSourceFile(req.getFileName());

        upload = sourceUploadRepository.save(upload);

        sourceService.process(upload);

        return upload;
    }

    @PostMapping("/convert/{id}")
    SourceUpload convert(@PathVariable Long id) throws Exception {
        SourceUpload upload = sourceUploadRepository.findById(id).orElseThrow();

        sourceService.process(upload);

        return upload;
    }

    @GetMapping("/view")
    public FileURLResponse view(@RequestParam("file") String fileName) throws Exception {
        return FileURLResponse.builder().url(fileService.objectURL(fileName)).build();
    }

    @GetMapping("/info/{id}")
    FileInfoResponse fileInfo(@PathVariable Long id) throws Exception {
        SourceUpload upload = sourceUploadRepository.findById(id).orElseThrow();

        StatObjectResponse stat = fileService.fileInfo(upload.getSourceFile());

        return FileInfoResponse.builder()
                .contentType(stat.contentType())
                .size(stat.size())
                .lastModified(stat.lastModified().toEpochSecond())
                .build();
    }
}
