package com.example.loginandsignup;

public class ReportModel {
    private int id;
    private int memberId;
    private String title;
    private String date;
    private String filePath;

    public ReportModel(int id, int memberId, String title, String date, String filePath) {
        this.id = id;
        this.memberId = memberId;
        this.title = title;
        this.date = date;
        this.filePath = filePath;
    }

    public int getId() { return id; }
    public int getMemberId() { return memberId; }
    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getFilePath() { return filePath; }

    public void setTitle(String title) {
        this.title = title;
    }
}
