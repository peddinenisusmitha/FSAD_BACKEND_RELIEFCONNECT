package com.reliefconnect.backend.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.reliefconnect.backend.entity.Donation;

public interface DonationRepository extends JpaRepository<Donation, Long> {
    List<Donation> findByDonorEmail(String donorEmail);
}