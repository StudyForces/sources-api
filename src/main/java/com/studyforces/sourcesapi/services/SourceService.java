package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.models.SourceUploadFile;
import com.studyforces.sourcesapi.models.UploadConvertedFile;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.responses.FileInfoResponse;
import com.studyforces.sourcesapi.responses.FileUploadResponse;
import com.studyforces.sourcesapi.services.messages.sourceProcess.*;
import com.studyforces.sourcesapi.models.SourceMetadata;
import io.minio.StatObjectResponse;
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
        req.setFileInfos(getFileInfo(upload.getSourceFiles()));

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
            req.setFileInfos(getFileInfo(upload.getSourceFiles()));

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

            List<String> files = msg.getData().getFiles();
            List<UploadConvertedFile> convs = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                UploadConvertedFile conv = new UploadConvertedFile();
                conv.setFile(files.get(i));
                conv.setPage(i);
                convs.add(conv);
            }

            upload.setConvertedFiles(convs);

            this.sourceUploadRepository.save(upload);
        };
    }

    private List<FileInfo> getFileInfo(List<SourceUploadFile> files) {
        return files.stream().map(file -> {
            try {
                StatObjectResponse stat = fileService.fileInfo(file.getFile());
                String url = fileService.objectURL(file.getFile());
                FileInfoResponse fi = FileInfoResponse.builder()
                        .contentType(stat.contentType())
                        .size(stat.size())
                        .lastModified(stat.lastModified().toEpochSecond())
                        .build();
                return FileInfo.builder()
                        .url(url)
                        .fileInfo(fi)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

}