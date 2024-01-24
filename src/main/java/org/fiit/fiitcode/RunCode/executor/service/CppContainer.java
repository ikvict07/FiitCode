package org.fiit.fiitcode.RunCode.executor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class CppContainer implements Container {
    private static final String path = "C:/Users/ikvict/FiitCode/src/main/resources"; //TODO : change to relative path
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
    public void sendCode(String codeFileName) throws IOException, InterruptedException {
        if (!isContainerRunning) {
            throw new IllegalStateException("Container is not running");
        }
        if (codeFileName == null) {
            throw new IllegalStateException("Code file name is not set");
        }

        sendFile(codeFileName, "cpp", "");

        // Add logging to list files in the Docker container's root directory
        ProcessBuilder processBuilder = new ProcessBuilder();
        executeLs(processBuilder);
    }

    @Override
    public void runCode(String inputFileName) throws IOException, InterruptedException {
        Process process = getProcess(inputFileName);
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

    private Process getProcess(String inputFileName) throws IOException {
        if (!isContainerRunning) {
            throw new IllegalStateException("Container is not running");
        }
        String inputFilePath = "/" + inputFileName;
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec " + containerName + " /bin/bash -c \"g++ /" + codeFileName + ".cpp -o /" + codeFileName + " && /" + codeFileName + " < " + inputFilePath + ".txt" + "\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        return process;
    }

    public void sendFile(String fileName, String extension, String dir) {
        System.out.println("Sending file " + path + dir + "/" + fileName + "." + extension + " to container " + containerName);
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "docker cp " + path + dir + "/" + fileName + "." + extension + " " + containerName + ":/" + fileName + "." + extension;
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

    private void generateTxtFileInsideContainer(String fileName, String content) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "docker exec " + containerName + " /bin/bash -c \"echo -e \\\"" + content + "\\\" > /home/" + fileName + ".txt\"";
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        try {
            int exitCode = process.waitFor();
            System.out.println("Exited with code: " + exitCode);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void deleteFileInsideContainer(String fileName) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        String command = "docker exec " + containerName + " /bin/bash -c \"rm /" + fileName + "\"";
        processBuilder.command("cmd.exe", "/c", command);
        processBuilder.start().waitFor();
    }
}
