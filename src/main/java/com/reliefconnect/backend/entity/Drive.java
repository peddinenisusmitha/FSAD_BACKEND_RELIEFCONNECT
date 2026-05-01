package com.reliefconnect.backend.entity;

import jakarta.persistence.*;

@Entity
public class Drive {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String driveName;
    private String location;
    private String date;
    private String driveType;
    private String itemsNeeded;
    private String status;

    public Drive() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDriveName() { return driveName; }
    public void setDriveName(String driveName) { this.driveName = driveName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getDriveType() { return driveType; }
    public void setDriveType(String driveType) { this.driveType = driveType; }

    public String getItemsNeeded() { return itemsNeeded; }
    public void setItemsNeeded(String itemsNeeded) { this.itemsNeeded = itemsNeeded; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}