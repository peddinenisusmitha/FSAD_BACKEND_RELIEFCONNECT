package com.reliefconnect.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reliefconnect.backend.entity.Donation;
import com.reliefconnect.backend.repository.DonationRepository;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin("*")
public class DonationController {

    @Autowired
    private DonationRepository donationRepository;

    // CREATE
    @PostMapping
    public ResponseEntity<Donation> addDonation(@RequestBody Donation donation) {
        donation.setStatus("Pending");
        return ResponseEntity.ok(donationRepository.save(donation));
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<Donation>> getAllDonations() {
        return ResponseEntity.ok(donationRepository.findAll());
    }

    // READ BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Donation> getDonationById(@PathVariable Long id) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));
        return ResponseEntity.ok(donation);
    }

    // ✅ UPDATE FULL (keep this for full edit forms)
    @PutMapping("/{id}")
    public ResponseEntity<Donation> updateDonation(@PathVariable Long id, @RequestBody Donation newDonation) {
        Donation d = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        d.setDriveName(newDonation.getDriveName());
        d.setItem(newDonation.getItem());
        d.setQuantity(newDonation.getQuantity());
        d.setStatus(newDonation.getStatus());
        d.setDonorEmail(newDonation.getDonorEmail());
        d.setDate(newDonation.getDate());

        return ResponseEntity.ok(donationRepository.save(d));
    }

    // 🔥 NEW: UPDATE ONLY STATUS (THIS FIXES YOUR BUTTON ISSUE)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Donation> updateStatus(@PathVariable Long id, @RequestBody Donation statusUpdate) {
        Donation d = donationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donation not found"));

        d.setStatus(statusUpdate.getStatus());

        return ResponseEntity.ok(donationRepository.save(d));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDonation(@PathVariable Long id) {
        donationRepository.deleteById(id);
        return ResponseEntity.ok("Donation Deleted Successfully");
    }
}