package com.colessok.api.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.colessok.api.file.entity.File;

@Repository
public interface FileMgmtRepository extends JpaRepository<File, String> {}
