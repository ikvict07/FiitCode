package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ServerFileSystem {
    public Optional<String> createFile(String fileName, String extension, String dir, String content) {
        Path fullPath = Paths.get(dir + fileName + "." + extension);
        System.out.println(dir + fileName + "." + extension);
        try {
            Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, content.getBytes());
        } catch (IOException e) {
            return Optional.empty();
        }
        System.out.println("File created");
        return Optional.of(fileName);
    }
}
