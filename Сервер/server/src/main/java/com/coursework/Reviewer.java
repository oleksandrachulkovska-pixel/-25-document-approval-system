package com.coursework;

public class Reviewer {
    private Long id;
    private String fullName;
    private String email;
    private String department;
    private String position;

    public Reviewer() {
    }

    public Reviewer(Long id, String fullName, String email,
                    String department, String position) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.position = position;
    }
}