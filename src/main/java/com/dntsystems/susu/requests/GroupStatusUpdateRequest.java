package com.dntsystems.susu.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class GroupStatusUpdateRequest {
    @Schema(description = "New status value", example = "OPENED/CLOSED")
    private String status;

    public GroupStatusUpdateRequest() {
    }

    public GroupStatusUpdateRequest(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
