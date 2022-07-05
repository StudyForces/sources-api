package com.studyforces.sourcesapi.services;

import com.studyforces.sourcesapi.models.Problem;
import com.studyforces.sourcesapi.repositories.ProblemRepository;
import com.studyforces.sourcesapi.services.messages.core.ProblemSyncMessage;
import com.studyforces.sourcesapi.services.messages.ocr.OCRDataText;
import com.studyforces.sourcesapi.services.messages.ocr.OCRResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

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
            problem.setAttachments(msg.getAttachments());
            problem = problemRepository.save(problem);
            if (msg.getSourcesId() == null) {
                syncProblem(problem);
            }
        };
    }

}
