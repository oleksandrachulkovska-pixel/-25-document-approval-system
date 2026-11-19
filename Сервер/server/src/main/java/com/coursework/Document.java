package com.coursework;

import java.time.LocalDateTime;

public class Document {

    private Long id;
    private String title;
    private String description;
    private String documentType;
    private String fileName;
    private LocalDateTime createdAt;
    private Long authorId;

    public Document() {
    }

    public Document(Long id, String title, String description,
                    String documentType, String fileName,
                    LocalDateTime createdAt, Long authorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.documentType = documentType;
        this.fileName = fileName;
        this.createdAt = createdAt;
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }
}