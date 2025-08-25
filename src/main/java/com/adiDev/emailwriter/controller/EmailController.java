package com.adiDev.emailwriter.controller;

import com.adiDev.emailwriter.EmailGeneratorService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins="*")
@AllArgsConstructor
public class EmailController {
    @Autowired
    private  final EmailGeneratorService service;
    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest email){
        String response=service.generatEmailReply(email);
        System.out.print("hit");
        return ResponseEntity.ok(response);
    }
}
