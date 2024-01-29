package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.AddInputRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class InputManager {
    @Value("$fiitcode.root.resources.inputs")
    private static String INPUTS_PATH;
    private final ServerFileSystem serverFileSystem;

    public InputManager(ServerFileSystem serverFileSystem) {
        this.serverFileSystem = serverFileSystem;
    }

    public void addInput(AddInputRequest request) {
        String path = INPUTS_PATH + request.getTaskId() + "/";
        String fileName = "input_" + request.getTaskId() + "_" + request.getIndex() + ".txt";
        serverFileSystem.createFile(fileName, "txt", path, request.getInput());
    }

    public List<String> findInputFiles(String taskId) {
        String path = INPUTS_PATH + taskId + "/";
        List<String> fileNames = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path), "input_" + taskId + "_*.txt")) {
            for (Path entry : stream) {
                fileNames.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("file names: " + fileNames);
        return fileNames;
    }
}
