package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import com.studyforces.sourcesapi.services.messages.sourceProcess.SourceConversion;
import com.studyforces.sourcesapi.models.SourceMetadata;
import com.studyforces.sourcesapi.services.messages.sourceProcess.SourceConversionRequest;
import com.studyforces.sourcesapi.services.messages.sourceProcess.SourceMetadataRequest;
import com.studyforces.sourcesapi.services.messages.sourceProcess.SourceProcessResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Transactional
@Service
public class SourceService {

    SourceService(FileService fileService, SourceUploadRepository sourceUploadRepository) {
        this.fileService = fileService;
        this.sourceUploadRepository = sourceUploadRepository;
    }

    FileService fileService;
    SourceUploadRepository sourceUploadRepository;

    BlockingQueue<SourceMetadataRequest> unboundedMetadataRequests = new LinkedBlockingQueue<>();
    BlockingQueue<SourceConversionRequest> unboundedConvertRequests = new LinkedBlockingQueue<>();

    public void process(SourceUpload upload) throws Exception {
        SourceMetadataRequest req = new SourceMetadataRequest();

        req.setSourceUploadID(upload.getId());
        req.setUrl(fileService.objectURL(upload.getSourceFile()));

        unboundedMetadataRequests.offer(req);
    }

    @Bean
    public Supplier<SourceMetadataRequest> sourceMetadataRequestSupplier() {
        return () -> unboundedMetadataRequests.poll();
    }

    @Bean
    public Consumer<SourceProcessResponse<SourceMetadata>> sourceMetadataSink() {
        return msg -> {
            SourceUpload upload = this.sourceUploadRepository.findById(msg.getSourceUploadID()).orElseThrow();
            upload.setMetadata(msg.getData());
            this.sourceUploadRepository.save(upload);

            SourceConversionRequest req = new SourceConversionRequest();
            req.setSourceUploadID(upload.getId());
            req.setMetadata(upload.getMetadata());
            try {
                req.setUrl(fileService.objectURL(upload.getSourceFile()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            List<FileUploadResponse> uploadURLs = new ArrayList<>();

            for (int i = 0; i < upload.getMetadata().getPages(); i++) {
                String fileName = "processed/sourceUpload-"+upload.getId()+"/page"+i+".png";
                FileUploadResponse res = new FileUploadResponse();
                res.setFileName(fileName);

                try {
                    res.setUrl(fileService.uploadURL(fileName, "image/png"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                uploadURLs.add(res);
            }

            req.setUploadURLs(uploadURLs);

            this.unboundedConvertRequests.offer(req);
        };
    }

    @Bean
    public Supplier<SourceConversionRequest> sourceConvertRequestSupplier() {
        return () -> unboundedConvertRequests.poll();
    }

    @Bean
    public Consumer<SourceProcessResponse<SourceConversion>> sourceConversionSink() {
        return msg -> {
            SourceUpload upload = this.sourceUploadRepository.findById(msg.getSourceUploadID()).orElseThrow();
            upload.setConvertedFiles(msg.getData().getFiles());
            this.sourceUploadRepository.save(upload);
        };
    }

}
