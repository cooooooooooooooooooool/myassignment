package com.myassign.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myassign.model.entity.Institute;

public interface InstituteRepository extends JpaRepository<Institute, String> {

    public Institute findOneByName(String instituteName);
}