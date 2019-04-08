package com.jm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jm.entity.Institute;

public interface InstituteRepository extends JpaRepository<Institute, String> {
	
	public Institute findOneByName(String instituteName);
}