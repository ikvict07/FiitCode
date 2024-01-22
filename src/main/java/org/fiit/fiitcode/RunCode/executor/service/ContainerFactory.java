package org.fiit.fiitcode.RunCode.executor.service;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.springframework.stereotype.Service;

@Service
public class ContainerFactory {
    public static Container createContainer(RunRequest request) {
        return switch (request.getLanguage()) {
            case Python -> new PythonContainer();
            case Cpp -> new CppContainer();
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }
}
