package com.reliefconnect.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reliefconnect.backend.entity.Drive;
import com.reliefconnect.backend.repository.DriveRepository;

@RestController
@RequestMapping("/api/drives")
@CrossOrigin("*")
public class DriveController {

    @Autowired
    private DriveRepository driveRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Drive> createDrive(@RequestBody Drive drive) {
        drive.setStatus("Active");
        return ResponseEntity.ok(driveRepository.save(drive));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Drive>> getAllDrives() {
        return ResponseEntity.ok(driveRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Drive> getDriveById(@PathVariable Long id) {
        Drive drive = driveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drive not found"));
        return ResponseEntity.ok(drive);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<Drive> updateDrive(@PathVariable Long id, @RequestBody Drive newDrive) {
        Drive d = driveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drive not found"));

        d.setDriveName(newDrive.getDriveName());
        d.setLocation(newDrive.getLocation());
        d.setDate(newDrive.getDate());
        d.setDriveType(newDrive.getDriveType());
        d.setItemsNeeded(newDrive.getItemsNeeded());
        d.setStatus(newDrive.getStatus());

        return ResponseEntity.ok(driveRepository.save(d));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDrive(@PathVariable Long id) {
        driveRepository.deleteById(id);
        return ResponseEntity.ok("Drive Deleted Successfully");
    }
}