package com.reliefconnect.backend.entity;

import jakarta.persistence.*;

@Entity
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driveName;
    private String item;
    private int quantity;
    private String status;
    private String donorEmail;
    private String date;

    public Donation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDriveName() { return driveName; }
    public void setDriveName(String driveName) { this.driveName = driveName; }

    public String getItem() { return item; }
    public void setItem(String item) { this.item = item; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDonorEmail() { return donorEmail; }
    public void setDonorEmail(String donorEmail) { this.donorEmail = donorEmail; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}