package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.Enums.SupportedLanguages;
import org.fiit.fiitcode.RunCode.executor.service.container.Container;
import org.fiit.fiitcode.RunCode.executor.service.container.ContainerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope("prototype")
public class Executor {
    private final InputManager inputManager;
    private final List<String> outputFileNames = new ArrayList<>();
    private Container container;

    public Executor(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void execute(String taskId, String fileName, SupportedLanguages language, List<String> inputFiles, String pathToFile) throws IOException, InterruptedException {
        container = createContainer(language);
        container.sendFile(fileName, language.getExtension(), pathToFile); //Sending code file inside container
        container.setFileName(fileName);

        runCodeForEachTest(inputFiles, taskId);
    }

    private Container createContainer(SupportedLanguages language) {
        Container container = ContainerFactory.createContainer(language);
        try {
            container.runContainer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return container;
    }


    private void runCodeForEachTest(List<String> inputFiles, String taskId) throws IOException, InterruptedException {
        //String codeFileName = createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));


        for (int i = 0; i < inputFiles.size(); i++) {
            String inputFileName = inputFiles.get(i).replace(".txt", "");
            String outputFileName = "output_" + i;

            container.sendFile(inputFileName, "txt", inputManager.getInputDir() + taskId + "/");
            container.runCode(inputFileName, outputFileName);
            outputFileNames.add(outputFileName);
        }
    }

    private String getContentFromFile(String fullFileName) throws IOException, InterruptedException {
        return container.getContentFromFile(fullFileName);
    }

    public List<String> getOutputs() {
        List<String> outputs = new ArrayList<>();
        for (String outputFileName : outputFileNames) {
            try {
                String content = getContentFromFile("/" + outputFileName + ".txt");
                outputs.add(content);
                System.out.println("Output: " + content);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return outputs;
    }

}
