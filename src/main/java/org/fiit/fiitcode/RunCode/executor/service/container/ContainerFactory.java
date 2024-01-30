package org.fiit.fiitcode.RunCode.executor.service.container;

import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.Enums.SupportedLanguages;
import org.springframework.stereotype.Service;

@Service
public class ContainerFactory {
    public static Container createContainer(SupportedLanguages language) {
        return switch (language) {
            case Python -> new PythonContainer(Container.generateContainerName());
            case Cpp -> new CppContainer(Container.generateContainerName());
            default -> throw new IllegalArgumentException("Unsupported language");
        };
    }
}
