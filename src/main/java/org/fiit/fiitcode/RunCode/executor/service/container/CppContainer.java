package org.fiit.fiitcode.RunCode.executor.service.container;

import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class CppContainer implements Container {
    public final String containerName;
    private boolean isContainerRunning;
    private String codeFileName;

    public CppContainer(String containerName) {
        this.containerName = containerName;
    }

    public static String generateCodeFileName() {
        return "code_" + System.currentTimeMillis() + new Random().nextInt(1000);
    }

    @Override
    public boolean isContainerRunning() {
        return isContainerRunning;
    }

    @Override
    public void setContainerRunning(boolean containerRunning) {
        this.isContainerRunning = containerRunning;
    }

    @Override
    public void setFileName(String fileName) {
        this.codeFileName = fileName;
    }

    @Override
    public void runContainer() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker run -d --name " + containerName + " gcc:latest sleep infinity");
        processBuilder.start().waitFor();
        setContainerRunning(true);
    }

    @Override
    public void runCode(String inputFileName, String outputFileName) throws IOException, InterruptedException {
        Process process = getProcessOfCodeExecution(inputFileName, outputFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        System.out.println("Output of running command is:");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("\nEnd");
        System.out.println("\nExited with error code : " + exitCode);
    }

    private Process getProcessOfCodeExecution(String inputFileName, String outputFileName) throws IOException {
        if (!isContainerRunning) {
            throw new IllegalStateException("Container is not running");
        }
        String inputFilePath = "/" + inputFileName;
        String outputFilePath = "/" + outputFileName;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec " + containerName + " /bin/bash -c \"g++ /" + codeFileName + ".cpp -o /" + codeFileName + " && /" + codeFileName + " < " + inputFilePath + ".txt > " + outputFilePath + ".txt\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return process;
    }

    public void sendFile(String fileName, String extension, String dir) {
        System.out.println("Sending file " + dir + fileName + "." + extension + " to container " + containerName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "docker cp " + dir + fileName + "." + extension + " " + containerName + ":/" + fileName + "." + extension;
        processBuilder.command("cmd.exe", "/c", command);
        try {
            processBuilder.start().waitFor();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            executeLs(processBuilder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getContentFromFile(String fullFileName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec " + containerName + " cat " + fullFileName);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        process.waitFor();
        return content.toString();
    }

    private void executeLs(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        processBuilder.command("cmd.exe", "/c", "docker exec " + containerName + " ls /");
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
    }

}
