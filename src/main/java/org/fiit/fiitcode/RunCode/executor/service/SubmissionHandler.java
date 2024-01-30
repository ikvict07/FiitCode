package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SubmissionHandler {
    private final Executor userCodeExecutor;
    private final Executor exampleCodeExecutor;
    private final CodeManager codeManager;
    private final InputManager inputManager;

    public SubmissionHandler(Executor exampleCodeExecutor, Executor userCodeExecutor, CodeManager codeManager, InputManager inputManager) {
        this.exampleCodeExecutor = exampleCodeExecutor;
        this.userCodeExecutor = userCodeExecutor;
        this.codeManager = codeManager;
        this.inputManager = inputManager;
    }

    public void submit(RunRequest runRequest) throws IOException, InterruptedException {
        if (!codeManager.isExampleCodeAvailable(runRequest.getTaskId(), runRequest.getLanguage())) {
            throw new RuntimeException("Example code is not available");
        }
        String userCodeFileName = codeManager.createCodeFile(runRequest).orElseThrow(() -> new RuntimeException("Code file name is not set"));
        String exampleCodeFileName = codeManager.getExampleCodeFileName(runRequest.getTaskId());
        List<String> inputFiles = inputManager.findInputFiles(runRequest.getTaskId());

        exampleCodeExecutor.execute(runRequest.getTaskId(), exampleCodeFileName, runRequest.getLanguage(), inputFiles, codeManager.getExampleDir());
        userCodeExecutor.execute(runRequest.getTaskId(), userCodeFileName, runRequest.getLanguage(), inputFiles, codeManager.getSolutionDir());

        List<String> exampleOutputs = exampleCodeExecutor.getOutputs();
        List<String> userOutputs = userCodeExecutor.getOutputs();

        for (int i = 0; i < exampleOutputs.size(); i++) {
            System.out.println("Example output: " + exampleOutputs.get(i));
            System.out.println("User output: " + userOutputs.get(i));
        }

    }

    public void test(RunRequest runRequest) throws IOException, InterruptedException {

        String userCodeFileName = codeManager.createCodeFile(runRequest).orElseThrow(() -> new RuntimeException("Code file name is not set"));

        List<String> inputFiles = inputManager.findInputFiles(runRequest.getTaskId());


        userCodeExecutor.execute(runRequest.getTaskId(), userCodeFileName, runRequest.getLanguage(), inputFiles, codeManager.getSolutionDir());

        List<String> userOutputs = userCodeExecutor.getOutputs();
    }
}
