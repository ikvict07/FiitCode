package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CodeManager {
    @Value("$fiitcode.root.resources.solutions")
    private static String SOLUTIONS_PATH;
    private final ServerFileSystem serverFileSystem;

    public CodeManager(ServerFileSystem serverFileSystem) {
        this.serverFileSystem = serverFileSystem;
    }

    public Optional<String> createCodeFile(RunRequest request) {
        String fileName = "solution_" + request.getTaskId() + "_" + request.getAisId();
        String extension = request.getLanguage().getExtension();
        System.out.println("Code file created");
        return serverFileSystem.createFile(fileName, extension, SOLUTIONS_PATH, request.getCode());
    }
}
