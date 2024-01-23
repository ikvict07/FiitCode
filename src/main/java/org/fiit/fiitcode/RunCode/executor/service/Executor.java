package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.AddInputRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class Executor {
    private static final String path = "C:\\Users\\ikvict\\FiitCode\\src\\main\\resources";
    public void execute(RunRequest request) {
        Container container = createContainer(request);
    }

    public Container createContainer(RunRequest request) {
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

    public Map<Integer, List<String>> findInputFiles(RunRequest request) throws IOException {
        int index = 0;

        String path = "/";
        String fileName = "input_" + request.getTaskId() + "_" + index + ".txt";

        Map<Integer, List<String>> inputFiles = new HashMap<>();
        while (Files.exists(Paths.get(path + fileName))) {
            index++;

            List<String> lines = Files.readAllLines(Paths.get(path + fileName));

            inputFiles.put(index, lines);
            fileName = "input_" + request.getTaskId() + "_" + index + ".txt";
        }
        return inputFiles;
    }

    public Map<Integer, List<String>> findOutputFiles(RunRequest request) throws IOException {
        int index = 0;

        String path = "/";
        String fileName = "output_" + request.getTaskId() + "_" + index + ".txt";

        Map<Integer, List<String>> outputFiles = new HashMap<>();
        while (Files.exists(Paths.get(path + fileName))) {
            index++;

            List<String> lines = Files.readAllLines(Paths.get(path + fileName));

            outputFiles.put(index, lines);
            fileName = "output_" + request.getTaskId() + "_" + index + ".txt";
        }
        return outputFiles;
    }

    private Optional<String> createCodeFile(RunRequest request) {
        String path = "/";
        String fileName = "solution_" + request.getTaskId() + "_" + request.getAisId() + ".cpp";
        Path fullPath = Paths.get(path + fileName);
        try {
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.write(fullPath, request.getCode().getBytes());
        } catch (IOException e) {
            return Optional.empty();
        }

        return Optional.of(fileName);
    }

    public void runCodeForEachTest(RunRequest request, Container container, Map<Integer, List<String>> inputFiles) throws IOException, InterruptedException {
        String codeFileName = createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));
        container.sendCode(codeFileName);

        int index = 0;
        while (inputFiles.containsKey(index)) {
            List<String> inputs = inputFiles.get(index);
            container.runCode(inputs);
            index++;
        }
    }

    public void addInput(AddInputRequest request) {
        String path = Executor.path + "/inputs/" + request.getTaskId() + "/";
        String fileName = "input_" + request.getTaskId() + "_" + request.getIndex() + ".txt";
        Path fullPath = Paths.get(path + fileName);
        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, request.getInput().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
