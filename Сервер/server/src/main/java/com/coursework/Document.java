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
}