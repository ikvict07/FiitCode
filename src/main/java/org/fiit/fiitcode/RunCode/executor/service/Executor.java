package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.service.container.Container;
import org.fiit.fiitcode.RunCode.executor.service.container.ContainerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class Executor {
    private final InputManager inputManager;
    private final CodeManager codeManager;
    private String codeFileName;

    private List<String> outputFileNames = new ArrayList<>();

    public Executor(CodeManager codeManager, InputManager inputManager) {
        this.codeManager = codeManager;
        this.inputManager = inputManager;
    }

    public void execute(RunRequest request) {
        Container container = createContainer(request);
        try {
            codeFileName = codeManager.createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));
            container.setFileName(codeFileName);
            runCodeForEachTest(request, container, inputManager.findInputFiles(request.getTaskId()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Container createContainer(RunRequest request) {
        Container container = ContainerFactory.createContainer(request);
        try {
            container.runContainer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return container;
    }


    public void runCodeForEachTest(RunRequest request, Container container, List<String> inputFiles) throws IOException, InterruptedException {
        //String codeFileName = createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));
        container.sendCode(codeFileName);

        for (int i = 0; i < inputFiles.size(); i++) {
            String inputFileName = inputFiles.get(i).replace(".txt", "");
            String outputFileName = "output_" + i + ".txt";

            container.sendFile(inputFileName, "txt", "/inputs/" + request.getTaskId());
            container.runCode(inputFileName, outputFileName);
            outputFileNames.add(outputFileName);
        }
    }

}
