package org.fiit.fiitcode.RunCode.executor.DTO;

import lombok.Data;

@Data
public class AddInputRequest {
    String input;
    String taskId;
    int index;
}
