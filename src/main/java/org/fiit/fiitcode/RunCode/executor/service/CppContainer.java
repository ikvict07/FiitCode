package org.fiit.fiitcode.RunCode.executor.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class CppContainer implements Container {
    private static final String path = "C:/Users/ikvict/untitled/src/main/resources/"; //TODO : change to relative path
    private boolean isContainerRunning;
    private String containerName;
    private String codeFileName;

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
    public void runContainer() throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker run -d --name " + containerName + " gcc:latest sleep infinity");
        processBuilder.start().waitFor();
        setContainerRunning(true);
    }

    @Override
    public void sendCode() throws IOException, InterruptedException {
        if (!isContainerRunning) {
            throw new IllegalStateException("Container is not running");
        }
        if (codeFileName == null) {
            throw new IllegalStateException("Code file name is not set");
        }
        ProcessBuilder processBuilder = new ProcessBuilder();

        processBuilder.command("cmd.exe", "/c", "docker cp " + path + codeFileName + ".cpp " + containerName + ":/" + codeFileName + ".cpp");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --stop-timeout 60 gcc:latest sleep infinity");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --stop-timeout 0.5 gcc:latest sleep infinity");
        //processBuilder.command("cmd.exe", "/c", "docker run -d --name my_container --memory=512m --cpus=1 gcc:latest sleep infinity");

        processBuilder.start().waitFor();
    }

    @Override
    public void runCode() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", "docker exec" + containerName + "/bin/bash -c \"g++ /" + codeFileName + ".cpp -o /" + codeFileName + " && /" + codeFileName + "\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();
        System.out.println("\nExited with error code : " + exitCode);
    }

}
