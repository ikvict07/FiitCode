package org.fiit.fiitcode.RunCode.executor.service;

import java.io.IOException;

public interface Container {
    void runContainer() throws IOException, InterruptedException;
    void setContainerRunning(boolean containerRunning);
    boolean isContainerRunning();

    void runCode() throws IOException, InterruptedException;
    void sendCode() throws IOException, InterruptedException;
}
