package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.AddInputRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class Executor {
    private static final String path = "C:\\Users\\ikvict\\FiitCode\\src\\main\\resources";
    private String codeFileName;

    public void execute(RunRequest request) {
        Container container = createContainer(request);
        try {
            codeFileName = createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));
            container.setFileName(codeFileName);
            runCodeForEachTest(request, container, findInputFiles(request));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    public List<String> findInputFiles(RunRequest request) {
        String path = Executor.path + "/inputs/" + request.getTaskId() + "/";
        List<String> fileNames = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), "input_" + request.getTaskId() + "_*.txt")) {
            for (Path entry : stream) {
                fileNames.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("file names: " + fileNames);
        return fileNames;
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
        String path = Executor.path + "/";
        String fileName = "solution_" + request.getTaskId() + "_" + request.getAisId();
        Path fullPath = Paths.get(path + fileName + ".cpp");
        try {
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, request.getCode().getBytes());
        } catch (IOException e) {
            return Optional.empty();
        }
        System.out.println("Code file created");
        return Optional.of(fileName);
    }

    public void runCodeForEachTest(RunRequest request, Container container, List<String> inputFiles) throws IOException, InterruptedException {
        //String codeFileName = createCodeFile(request).orElseThrow(() -> new RuntimeException("Code file name is not set"));
        container.sendCode(codeFileName);

        for (String inputFileName : inputFiles) {
            inputFileName = inputFileName.replace(".txt", "");
            container.sendFile(inputFileName, "txt", "/inputs/" + request.getTaskId());
            container.runCode(inputFileName);
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
