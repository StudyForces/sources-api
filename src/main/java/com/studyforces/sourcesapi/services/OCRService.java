package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.models.SourceUploadRect;
import com.studyforces.sourcesapi.models.SourceUploadRectStatus;
import com.studyforces.sourcesapi.repositories.SourceUploadRepository;
import com.studyforces.sourcesapi.services.messages.OCRDataFormula;
import com.studyforces.sourcesapi.services.messages.OCRDataText;
import com.studyforces.sourcesapi.services.messages.OCRRequestMessage;
import com.studyforces.sourcesapi.services.messages.OCRResponseMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Transactional
@Service
public class OCRService {

    OCRService(FileService fileService, SourceUploadRepository sourceUploadRepository) {
        this.fileService = fileService;
        this.sourceUploadRepository = sourceUploadRepository;
    }

    FileService fileService;
    SourceUploadRepository sourceUploadRepository;

    BlockingQueue<OCRRequestMessage> unboundedTexts = new LinkedBlockingQueue<>();
    BlockingQueue<OCRRequestMessage> unboundedFormulas = new LinkedBlockingQueue<>();

    public SourceUpload runOCR(@NotNull SourceUpload upload) throws Exception {
        List<SourceUploadRect> rects = upload.getRects().stream().toList();

        for (SourceUploadRect rect : rects) {
            rect.setStatus(SourceUploadRectStatus.PENDING);
            OCRRequestMessage msg = new OCRRequestMessage();
            msg.setSourceUploadID(upload.getId());
            msg.setSourceUploadURL(fileService.objectURL(upload.getSourceFile()));
            msg.setRect(rect);
            switch (rect.getType()) {
                case TEXT -> unboundedTexts.offer(msg);
                case FORMULA -> unboundedFormulas.offer(msg);
            }
        }

        this.sourceUploadRepository.save(upload);

        return upload;
    }

    @Bean
    public Supplier<OCRRequestMessage> textSupplier() {
        return () -> unboundedTexts.poll();
    }

    @Bean
    public Supplier<OCRRequestMessage> formulaSupplier() {
        return () -> unboundedFormulas.poll();
    }

    private <T> void processSinked(OCRResponseMessage<T> msg) {
        SourceUpload upload = this.sourceUploadRepository.findById(msg.getSourceUploadID()).orElseThrow();

        for (SourceUploadRect rect : upload.getRects()) {
            if (rect.equals(msg.getRect())) {
                rect.setStatus(SourceUploadRectStatus.DONE);
                rect.setData(msg.getData());
                break;
            }
        }

        this.sourceUploadRepository.save(upload);
    }

    @Bean
    public Consumer<OCRResponseMessage<OCRDataText>> textSink() {
        return this::processSinked;
    }

    @Bean
    public Consumer<OCRResponseMessage<OCRDataFormula>> formulaSink() {
        return this::processSinked;
    }

}
