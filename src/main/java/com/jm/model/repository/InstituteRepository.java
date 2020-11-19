package com.jm.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.model.entity.Institute;

public interface InstituteRepository extends JpaRepository<Institute, String> {

    public Institute findOneByName(String instituteName);
}