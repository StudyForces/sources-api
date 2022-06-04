package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.models.SourceUploadRect;
import com.studyforces.sourcesapi.models.SourceUploadRectStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

@Service
public class OCRService {

    OCRService(FileService fileService) {
        this.fileService = fileService;
    }

    FileService fileService;

    BlockingQueue<OCRMessage> unboundedTexts = new LinkedBlockingQueue<>();

    public void runOCR(SourceUpload upload) throws Exception {
        List<SourceUploadRect> rects = upload.getRects().stream().toList();

        for (SourceUploadRect rect : rects) {
            rect.setStatus(SourceUploadRectStatus.PENDING);
            OCRMessage msg = new OCRMessage();
            msg.setSourceUploadID(upload.getId());
            msg.setSourceUploadURL(fileService.objectURL(upload.getSourceFile()));
            unboundedTexts.offer(msg);
        }
    }

    @Bean
    public Supplier<OCRMessage> textSupplier() {
        return () -> unboundedTexts.poll();
    }

}
