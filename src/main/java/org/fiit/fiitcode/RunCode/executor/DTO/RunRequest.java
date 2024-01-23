package org.fiit.fiitcode.RunCode.executor.DTO;

import lombok.Data;
import org.fiit.fiitcode.RunCode.executor.Enums.SupportedLanguages;

@Data
public class RunRequest {
    SupportedLanguages language;
    String aisId;
    String taskId;
    String code;
}
