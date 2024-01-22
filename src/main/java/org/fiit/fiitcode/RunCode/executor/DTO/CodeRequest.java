package org.fiit.fiitcode.RunCode.executor.DTO;

import lombok.Data;

@Data
public class CodeRequest {
    String code;
    String language;
    String aisId;
    String taskId;
}
