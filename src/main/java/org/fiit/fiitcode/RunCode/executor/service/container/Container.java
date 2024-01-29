package org.fiit.fiitcode.RunCode.executor.service.container;

import java.io.IOException;
import java.util.List;

public interface Container {
    static String generateContainerName() {
        return "container_" + System.currentTimeMillis() + Math.random();
    }

    void setFileName(String fileName);

    void runContainer() throws IOException, InterruptedException;

    boolean isContainerRunning();

    void setContainerRunning(boolean containerRunning);

    void runCode(String inputFileName, String outputFileName) throws IOException, InterruptedException;

    void sendCode(String fileName) throws IOException, InterruptedException;

    void sendFile(String fileName, String extension, String dir) throws IOException, InterruptedException;
}
