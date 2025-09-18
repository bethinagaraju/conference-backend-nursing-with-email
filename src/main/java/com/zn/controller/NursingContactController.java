package com.zn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zn.dto.ContactFormDto;
import com.zn.nursing.service.NursingEmailService;

@RestController
@RequestMapping("/api/nursing/contact")
public class NursingContactController {

    @Autowired
    private NursingEmailService nursingEmailService;

    @PostMapping
    public String sendContactMessage(@RequestBody ContactFormDto dto) {
        nursingEmailService.sendContactMessage(dto.getName(), dto.getEmail(), dto.getSubject(), dto.getMessage());
        return "Message sent successfully";
    }
}
