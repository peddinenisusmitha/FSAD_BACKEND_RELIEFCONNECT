package com.reliefconnect.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.reliefconnect.backend.entity.Drive;

public interface DriveRepository extends JpaRepository<Drive, Long> {
    List<Drive> findByStatus(String status);
}