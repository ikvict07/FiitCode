package org.fiit.fiitcode.RunCode.executor.controller;

import org.fiit.fiitcode.RunCode.executor.DTO.AddInputRequest;
import org.fiit.fiitcode.RunCode.executor.service.Executor;
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

    @Autowired
    public TaskController(Executor executor) {
        this.executor = executor;
    }

    @GetMapping("/send-user-solution")
    public ResponseEntity<?> sendUserSolution() {
        return new ResponseEntity<>(null, null, null);
    }

    @PostMapping("/add-input")
    public ResponseEntity<?> addInput(@RequestBody AddInputRequest addInputRequest) {
        executor.addInput(addInputRequest);
        return ResponseEntity.ok().build();
    }
}
