package org.fiit.fiitcode.RunCode.executor.service;

import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

public class CppContainer implements Container {
    private static final String path = "C:\\Users\\ikvict\\FiitCode\\src\\main\\resources"; //TODO : change to relative path
    private boolean isContainerRunning;
    public final String containerName;

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

        sendFile(codeFileName, "cpp");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --stop-timeout 60 gcc:latest sleep infinity");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --stop-timeout 0.5 gcc:latest sleep infinity");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --memory=512m --cpus=1 gcc:latest sleep infinity");

    }

    @Override
    public void runCode(List<String> inputs) throws IOException, InterruptedException {
        if (!isContainerRunning) {
            throw new IllegalStateException("Container is not running");
        }

        String inputFileName = "input";
        generateTxtFileInsideContainer(inputFileName, inputs.stream().reduce("", (a, b) -> a + "\n" + b));


        String inputFilePath = "/" + inputFileName + ".txt";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec" + containerName + "/bin/bash -c \"g++ /" + codeFileName + ".cpp -o /" + codeFileName + " && /" + codeFileName + " < " + inputFilePath + "\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();

        deleteFileInsideContainer(inputFileName);
        System.out.println("\nExited with error code : " + exitCode);
    }

    private void sendFile(String fileName, String extention) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        StringBuilder command = new StringBuilder();
        command.append("docker cp ").append(path).append(fileName).append(".").append(extention).append(" ").append(containerName).append(":/").append(fileName).append(extention);
        processBuilder.command("cmd.exe", "/c", command.toString());
        processBuilder.start().waitFor();
    }

    private void generateTxtFileInsideContainer(String fileName, String content) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec" + containerName + " echo " + content + " > " + "/" + fileName + ".txt");
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
