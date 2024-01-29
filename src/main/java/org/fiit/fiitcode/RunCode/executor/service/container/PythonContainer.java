package org.fiit.fiitcode.RunCode.executor.service.container;

import java.io.IOException;

public class PythonContainer implements Container {

    public final String containerName;

    public PythonContainer(String containerName) {
        this.containerName = containerName;
    }

    @Override
    public void setFileName(String fileName) {

    }

    @Override
    public void runContainer() {

    }

    @Override
    public void setContainerRunning(boolean containerRunning) {

    }

    @Override
    public boolean isContainerRunning() {
        return false;
    }

    @Override
    public void runCode(String inputFileName, String outputFileName) throws IOException, InterruptedException {

    }

    @Override
    public void sendCode(String fileCodeName) throws IOException, InterruptedException {

    }

    @Override
    public void sendFile(String fileName, String extension, String dir) throws IOException, InterruptedException {

    }
}
