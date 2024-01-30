package org.fiit.fiitcode.RunCode.executor.DTO;

import lombok.Data;
import org.fiit.fiitcode.RunCode.executor.Enums.SupportedLanguages;

@Data
public class AddExampleRequest {
    String code;
    SupportedLanguages language;
    String taskId;
}
