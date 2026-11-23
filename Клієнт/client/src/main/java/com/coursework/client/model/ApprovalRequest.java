package com.coursework.client.model;

import java.time.LocalDateTime;

public class ApprovalRequest {

    private Long id;
    private Long documentId;
    private Long reviewerId;
    private String status;

    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    private double f1;
    private double f2;
    private double f3;
    private double integralIndex;

    public ApprovalRequest() {
    }

    public ApprovalRequest(
            Long id,
            Long documentId,
            Long reviewerId,
            String status,
            LocalDateTime submittedAt,
            LocalDateTime reviewedAt,
            double f1,
            double f2,
            double f3,
            double integralIndex
    ) {
        this.id = id;
        this.documentId = documentId;
        this.reviewerId = reviewerId;
        this.status = status;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.integralIndex = integralIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(Long reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public double getF1() {
        return f1;
    }

    public void setF1(double f1) {
        this.f1 = f1;
    }

    public double getF2() {
        return f2;
    }

    public void setF2(double f2) {
        this.f2 = f2;
    }

    public double getF3() {
        return f3;
    }

    public void setF3(double f3) {
        this.f3 = f3;
    }

    public double getIntegralIndex() {
        return integralIndex;
    }

    public void setIntegralIndex(double integralIndex) {
        this.integralIndex = integralIndex;
    }
}