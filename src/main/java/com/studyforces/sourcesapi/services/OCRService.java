package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.OCRResult;
import com.studyforces.sourcesapi.models.SourceUpload;
import com.studyforces.sourcesapi.models.OCRResultStatus;
import com.studyforces.sourcesapi.repositories.OCRResultRepository;
import com.studyforces.sourcesapi.services.messages.ocr.OCRDataFormula;
import com.studyforces.sourcesapi.services.messages.ocr.OCRDataText;
import com.studyforces.sourcesapi.services.messages.ocr.OCRRequestMessage;
import com.studyforces.sourcesapi.services.messages.ocr.OCRResponseMessage;
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

    OCRService(FileService fileService, OCRResultRepository ocrResultRepository) {
        this.fileService = fileService;
        this.ocrResultRepository = ocrResultRepository;
    }

    FileService fileService;
    OCRResultRepository ocrResultRepository;

    BlockingQueue<OCRRequestMessage> unboundedTexts = new LinkedBlockingQueue<>();
    BlockingQueue<OCRRequestMessage> unboundedFormulas = new LinkedBlockingQueue<>();

    public SourceUpload runOCR(@NotNull SourceUpload upload) throws Exception {
        List<OCRResult> results = upload.getOcrResults().stream().toList();

        for (OCRResult result : results) {
            result.setStatus(OCRResultStatus.PENDING);
            OCRRequestMessage msg = new OCRRequestMessage();
            msg.setUrl(fileService.objectURL(upload.getSourceFile()));
            msg.setOcrResultID(result.getId());
            msg.setRect(result.getRect());
            switch (result.getType()) {
                case TEXT -> unboundedTexts.offer(msg);
                case FORMULA -> unboundedFormulas.offer(msg);
            }
        }

        this.ocrResultRepository.saveAll(results);

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
        OCRResult result = this.ocrResultRepository.findById(msg.getOcrResultID()).orElseThrow();

        result.setStatus(OCRResultStatus.DONE);
        result.setData(msg.getData());

        this.ocrResultRepository.save(result);
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
