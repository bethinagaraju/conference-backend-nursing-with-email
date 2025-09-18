package com.zn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zn.dto.ContactFormDto;
import com.zn.nursing.service.NursingEmailService;

@RestController
@RequestMapping("/api/nursing/contact-form")
public class NursingContactFormController {

    @Autowired
    private NursingEmailService nursingEmailService;

    @PostMapping
    public ResponseEntity<String> sendContactMessage(@RequestBody ContactFormDto dto) {
        try {
            nursingEmailService.sendContactMessage(dto.getName(), dto.getEmail(), dto.getSubject(), dto.getMessage());
            return ResponseEntity.ok("Message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to send message");
        }
    }
}
