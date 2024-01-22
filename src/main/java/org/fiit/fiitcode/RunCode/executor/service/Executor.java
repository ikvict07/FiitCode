package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Executor {

    public void execute(RunRequest request) {
        Container containerBuilder = ContainerFactory.createContainer(request);
        try {
            containerBuilder.runContainer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
