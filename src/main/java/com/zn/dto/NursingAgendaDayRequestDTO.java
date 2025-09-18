package com.zn.dto;

import lombok.Data;

@Data
public class NursingAgendaDayRequestDTO {
    private String day; // e.g., "Day 1"
    private String date; // e.g., "May 15, 2026"
}
