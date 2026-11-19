package com.coursework;

public class Author {

    private Long id;
    private String fullName;
    private String email;
    private String department;
    private String position;

    public Author() {
    }

    public Author(Long id, String fullName, String email,
                  String department, String position) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.department = department;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
