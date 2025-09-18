package com.zn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zn.dto.NursingAgendaDayRequestDTO;
import com.zn.dto.NursingAgendaSessionRequestDTO;
import com.zn.nursing.entity.NursingAgenda;
import com.zn.nursing.service.NursingAgendaService;

@RestController
@RequestMapping("/api/nursing-agenda")
public class NursingAgendaController {

    @Autowired
    private NursingAgendaService service;

    @GetMapping
    public ResponseEntity<NursingAgenda> getAgenda() {
        NursingAgenda agenda = service.getAgenda();
        if (agenda != null) {
            return ResponseEntity.ok(agenda);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<NursingAgenda> createAgenda(@RequestBody NursingAgenda agenda) {
        NursingAgenda saved = service.saveOrUpdateAgenda(agenda);
        return ResponseEntity.ok(saved);
    }

    @PutMapping
    public ResponseEntity<NursingAgenda> updateAgenda(@RequestBody NursingAgenda agenda) {
        NursingAgenda updated = service.saveOrUpdateAgenda(agenda);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/addDay")
    public ResponseEntity<NursingAgenda> addDay(@RequestBody NursingAgendaDayRequestDTO dayRequest) {
        NursingAgenda updated = service.addDay(dayRequest);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/addSession/{day}")
    public ResponseEntity<NursingAgenda> addSession(@PathVariable String day, @RequestBody NursingAgendaSessionRequestDTO sessionRequest) {
        NursingAgenda updated = service.addSession(day, sessionRequest);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/editSession/{day}/{sessionIndex}")
    public ResponseEntity<NursingAgenda> editSession(@PathVariable String day, @PathVariable int sessionIndex, @RequestBody NursingAgendaSessionRequestDTO sessionRequest) {
        NursingAgenda updated = service.editSession(day, sessionIndex, sessionRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteSession/{day}/{sessionIndex}")
    public ResponseEntity<NursingAgenda> deleteSession(@PathVariable String day, @PathVariable int sessionIndex) {
        NursingAgenda updated = service.deleteSession(day, sessionIndex);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/editDay/{day}")
    public ResponseEntity<NursingAgenda> editDay(@PathVariable String day, @RequestBody NursingAgendaDayRequestDTO dayRequest) {
        NursingAgenda updated = service.editDay(day, dayRequest);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/deleteDay/{day}")
    public ResponseEntity<NursingAgenda> deleteDay(@PathVariable String day) {
        NursingAgenda updated = service.deleteDay(day);
        return ResponseEntity.ok(updated);
    }
}
