package org.fiit.fiitcode.RunCode.executor.controller;

import org.fiit.fiitcode.RunCode.executor.DTO.AddExampleRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.service.CodeManager;
import org.fiit.fiitcode.RunCode.executor.service.SubmissionHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ReceiverController {
    private final SubmissionHandler submissionHandler;
    private final CodeManager codeManager;

    public ReceiverController(SubmissionHandler submissionHandler, CodeManager codeManager) {
        this.submissionHandler = submissionHandler;
        this.codeManager = codeManager;
    }

    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody RunRequest runRequest) {
        try {
            submissionHandler.submit(runRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-example")
    public ResponseEntity<?> addExample(@RequestBody AddExampleRequest request) {
        try {
            codeManager.createExampleCodeFile(request);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
