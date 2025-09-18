package com.zn.nursing.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zn.nursing.entity.NursingAgenda;

public interface INursingAgendaRepository extends JpaRepository<NursingAgenda, Long> {
    Optional<NursingAgenda> findFirstByOrderByIdAsc();
}
