package com.coursework;

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

    public ApprovalRequest(Long id, Long documentId, Long reviewerId,
                           String status, LocalDateTime submittedAt,
                           LocalDateTime reviewedAt, double f1,
                           double f2, double f3, double integralIndex) {
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
}