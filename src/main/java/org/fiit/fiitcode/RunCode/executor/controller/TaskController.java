package org.fiit.fiitcode.RunCode.executor.controller;

import org.fiit.fiitcode.RunCode.executor.DTO.AddInputRequest;
import org.fiit.fiitcode.RunCode.executor.service.Executor;
import org.fiit.fiitcode.RunCode.executor.service.InputManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/task")
public class TaskController {
    final Executor executor;
    final InputManager inputManager;

    @Autowired
    public TaskController(Executor executor, InputManager inputManager) {
        this.executor = executor;
        this.inputManager = inputManager;
    }

    @GetMapping("/send-user-solution")
    public ResponseEntity<?> sendUserSolution() {
        return new ResponseEntity<>(null, null, null);
    }

    @PostMapping("/add-input")
    public ResponseEntity<?> addInput(@RequestBody AddInputRequest addInputRequest) {
        inputManager.addInput(addInputRequest);
        return ResponseEntity.ok().build();
    }
}
