package org.fiit.fiitcode.RunCode.executor.controller;

import org.fiit.fiitcode.RunCode.executor.DTO.CodeRequest;
import org.fiit.fiitcode.RunCode.executor.DTO.RunRequest;
import org.fiit.fiitcode.RunCode.executor.service.Executor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Random;

@Controller
public class ReceiverController {
    private final Executor executor;

    public ReceiverController(Executor executor) {
        this.executor = executor;
    }

    @PostMapping("/run")
    public ResponseEntity<?> run(@RequestBody RunRequest runRequest) {
        executor.execute(runRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/save-code")
    public ResponseEntity<?> saveCode(@RequestBody CodeRequest codeRequest) {
        generateName(); //TODO :save code
        return ResponseEntity.ok().build();
    }

    private String generateName() {
        return "container_" + System.currentTimeMillis() + new Random().nextInt(1000);
    }
}
