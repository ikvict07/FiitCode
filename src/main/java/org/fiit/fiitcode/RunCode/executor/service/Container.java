package org.fiit.fiitcode.RunCode.executor.service;

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

    void runCode(List<String> inputs) throws IOException, InterruptedException;

    void sendCode(String fileName) throws IOException, InterruptedException;
}
