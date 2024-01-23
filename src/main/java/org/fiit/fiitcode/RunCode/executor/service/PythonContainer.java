package org.fiit.fiitcode.RunCode.executor.service;

import java.io.IOException;
import java.util.List;

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
    public void runCode(List<String> inputs) throws IOException, InterruptedException {

    }

    @Override
    public void sendCode(String fileCodeName) throws IOException, InterruptedException {

    }
}
