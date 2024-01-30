package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.AddExampleRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.Enums.SupportedLanguages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class CodeManager {
    private final ServerFileSystem serverFileSystem;
    @Value("${fiitcode.root.resources.solutions}")
    private String SOLUTIONS_PATH;
    @Value("${fiitcode.root.resources.solutions.examples}")
    private String EXAMPLES_PATH;

    public CodeManager(ServerFileSystem serverFileSystem) {
        this.serverFileSystem = serverFileSystem;
    }

    public Optional<String> createCodeFile(RunRequest request) {
        String fileName = "solution_" + request.getTaskId() + "_" + request.getAisId();
        String extension = request.getLanguage().getExtension();
        System.out.println("Code file created: " + SOLUTIONS_PATH + fileName + "." + extension);
        return serverFileSystem.createFile(fileName, extension, SOLUTIONS_PATH, request.getCode());
    }

    public Optional<String> createExampleCodeFile(AddExampleRequest request) {
        String fileName = "example_" + request.getTaskId();
        String extension = request.getLanguage().getExtension();
        System.out.println("Code file created: " + EXAMPLES_PATH + fileName + "." + extension);
        return serverFileSystem.createFile(fileName, extension, EXAMPLES_PATH, request.getCode());
    }

    public boolean isExampleCodeAvailable(String taskId, SupportedLanguages language) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(EXAMPLES_PATH), getExampleCodeFileName(taskId) + "." + language.getExtension())) {
            for (Path entry : stream) {
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public String getExampleCodeFileName(String taskId) {
        return "example_" + taskId;
    }

    public String getExampleDir() {
        return EXAMPLES_PATH;
    }
    public String getSolutionDir() {
        return SOLUTIONS_PATH;
    }
}
