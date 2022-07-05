package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.OCRResult;
import com.studyforces.sourcesapi.models.Problem;
import com.studyforces.sourcesapi.models.ProblemAttachment;
import com.studyforces.sourcesapi.repositories.ProblemRepository;
import com.studyforces.sourcesapi.services.messages.core.ProblemSyncMessage;
import com.studyforces.sourcesapi.services.messages.ocr.OCRDataText;
import com.studyforces.sourcesapi.services.messages.ocr.OCRResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class CoreService {

    private final ProblemRepository problemRepository;

    CoreService(ProblemRepository problemRepository) {
        this.problemRepository = problemRepository;
    }

    BlockingQueue<ProblemSyncMessage> unboundedMessages = new LinkedBlockingQueue<>();

    public void syncProblem(Problem problem) {
        unboundedMessages.offer(ProblemSyncMessage.builder()
                .sourcesId(problem.getId())
                .coreId(problem.getCoreId())
                .type(problem.getType())
                .problem(problem.getProblem())
                .solution(problem.getSolution())
                .solverMetadata(problem.getSolverMetadata())
                .attachments(problem.getAttachments())
                .build());
    }

    @Bean
    Supplier<ProblemSyncMessage> coreApiSupplier() {
        return () -> unboundedMessages.poll();
    }

    @Bean
    Consumer<ProblemSyncMessage> coreApiSink() {
        return msg -> {
            Problem problem;
            if (msg.getSourcesId() != null) {
                problem = problemRepository.findById(msg.getSourcesId()).orElse(new Problem());
            } else {
                problem = new Problem();
            }
            problem.setCoreId(msg.getCoreId());
            problem.setType(msg.getType());
            problem.setProblem(msg.getProblem());
            problem.setSolution(msg.getSolution());
            problem.setSolverMetadata(msg.getSolverMetadata());

            // Merge Attachments
            List<ProblemAttachment> prevAttachments = problem.getAttachments();
            if (prevAttachments == null) {
                prevAttachments = new ArrayList<>();
            }
            Map<String, ProblemAttachment> prevAtMap = prevAttachments.stream().collect(Collectors.toMap(ProblemAttachment::getFileName, i -> i));
            List<ProblemAttachment> mergedAttachments = msg.getAttachments().stream().peek(attachment -> {
                if (prevAtMap.containsKey(attachment.getFileName())) {
                    attachment.setMetadata(prevAtMap.get(attachment.getFileName()).getMetadata());
                }
            }).toList();

            problem.setAttachments(mergedAttachments);

            problem = problemRepository.save(problem);
            if (msg.getSourcesId() == null) {
                syncProblem(problem);
            }
        };
    }

}
