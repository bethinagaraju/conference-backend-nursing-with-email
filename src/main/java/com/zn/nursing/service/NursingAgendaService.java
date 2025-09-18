package com.zn.nursing.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zn.dto.NursingAgendaDayRequestDTO;
import com.zn.dto.NursingAgendaSessionRequestDTO;
import com.zn.nursing.entity.NursingAgenda;
import com.zn.nursing.repository.INursingAgendaRepository;

@Service
public class NursingAgendaService {

    @Autowired
    private INursingAgendaRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public NursingAgenda getAgenda() {
        Optional<NursingAgenda> agenda = repository.findFirstByOrderByIdAsc();
        return agenda.orElse(null);
    }

    public NursingAgenda saveOrUpdateAgenda(NursingAgenda agenda) {
        Optional<NursingAgenda> existing = repository.findFirstByOrderByIdAsc();
        if (existing.isPresent()) {
            NursingAgenda existingAgenda = existing.get();
            existingAgenda.setAgendaData(agenda.getAgendaData());
            return repository.save(existingAgenda);
        } else {
            return repository.save(agenda);
        }
    }

    public NursingAgenda addDay(NursingAgendaDayRequestDTO dayRequest) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        NursingAgenda agenda;
        Map<String, Object> agendaMap;

        if (existingOpt.isPresent()) {
            agenda = existingOpt.get();
            try {
                agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
            } catch (JsonProcessingException e) {
                agendaMap = new HashMap<>();
            }
        } else {
            agenda = new NursingAgenda();
            agendaMap = new HashMap<>();
        }

        Map<String, Object> dayData = new HashMap<>();
        dayData.put("date", dayRequest.getDate());
        dayData.put("sessions", new ArrayList<Map<String, String>>());

        agendaMap.put(dayRequest.getDay(), dayData);

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }

    public NursingAgenda addSession(String day, NursingAgendaSessionRequestDTO sessionRequest) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Agenda not found. Please add a day first.");
        }

        NursingAgenda agenda = existingOpt.get();
        Map<String, Object> agendaMap;

        try {
            agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing agenda data", e);
        }

        if (!agendaMap.containsKey(day)) {
            throw new RuntimeException("Day not found in agenda.");
        }

        Map<String, Object> dayData = (Map<String, Object>) agendaMap.get(day);
        List<Map<String, String>> sessions = (List<Map<String, String>>) dayData.get("sessions");

        Map<String, String> session = new HashMap<>();
        session.put("time", sessionRequest.getTime());
        session.put("title", sessionRequest.getTitle());
        session.put("description", sessionRequest.getDescription());

        sessions.add(session);

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }

    public NursingAgenda editSession(String day, int sessionIndex, NursingAgendaSessionRequestDTO sessionRequest) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Agenda not found.");
        }

        NursingAgenda agenda = existingOpt.get();
        Map<String, Object> agendaMap;

        try {
            agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing agenda data", e);
        }

        if (!agendaMap.containsKey(day)) {
            throw new RuntimeException("Day not found in agenda.");
        }

        Map<String, Object> dayData = (Map<String, Object>) agendaMap.get(day);
        List<Map<String, String>> sessions = (List<Map<String, String>>) dayData.get("sessions");

        if (sessionIndex < 0 || sessionIndex >= sessions.size()) {
            throw new RuntimeException("Session index out of bounds.");
        }

        Map<String, String> session = sessions.get(sessionIndex);
        session.put("time", sessionRequest.getTime());
        session.put("title", sessionRequest.getTitle());
        session.put("description", sessionRequest.getDescription());

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }

    public NursingAgenda deleteSession(String day, int sessionIndex) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Agenda not found.");
        }

        NursingAgenda agenda = existingOpt.get();
        Map<String, Object> agendaMap;

        try {
            agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing agenda data", e);
        }

        if (!agendaMap.containsKey(day)) {
            throw new RuntimeException("Day not found in agenda.");
        }

        Map<String, Object> dayData = (Map<String, Object>) agendaMap.get(day);
        List<Map<String, String>> sessions = (List<Map<String, String>>) dayData.get("sessions");

        if (sessionIndex < 0 || sessionIndex >= sessions.size()) {
            throw new RuntimeException("Session index out of bounds.");
        }

        sessions.remove(sessionIndex);

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }

    public NursingAgenda editDay(String day, NursingAgendaDayRequestDTO dayRequest) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Agenda not found.");
        }

        NursingAgenda agenda = existingOpt.get();
        Map<String, Object> agendaMap;

        try {
            agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing agenda data", e);
        }

        if (!agendaMap.containsKey(day)) {
            throw new RuntimeException("Day not found in agenda.");
        }

        Map<String, Object> dayData = (Map<String, Object>) agendaMap.get(day);
        dayData.put("date", dayRequest.getDate());

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }

    public NursingAgenda deleteDay(String day) {
        Optional<NursingAgenda> existingOpt = repository.findFirstByOrderByIdAsc();
        if (!existingOpt.isPresent()) {
            throw new RuntimeException("Agenda not found.");
        }

        NursingAgenda agenda = existingOpt.get();
        Map<String, Object> agendaMap;

        try {
            agendaMap = objectMapper.readValue(agenda.getAgendaData(), Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing agenda data", e);
        }

        if (!agendaMap.containsKey(day)) {
            throw new RuntimeException("Day not found in agenda.");
        }

        agendaMap.remove(day);

        try {
            agenda.setAgendaData(objectMapper.writeValueAsString(agendaMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing agenda data", e);
        }

        return repository.save(agenda);
    }
}
