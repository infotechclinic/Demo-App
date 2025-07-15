package com.example.loginandsignup;

public class PrescriptionModel {
    private int id;
    private int memberId;
    private String medicineName;
    private String dosage;
    private String date;

    public PrescriptionModel(int id, int memberId, String medicineName, String dosage, String date) {
        this.id = id;
        this.memberId = memberId;
        this.medicineName = medicineName;
        this.dosage = dosage;
        this.date = date;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public String getDosage() {
        return dosage;
    }

    public String getDate() {
        return date;
    }

    // Setters (if needed)
    public void setId(int id) {
        this.id = id;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
