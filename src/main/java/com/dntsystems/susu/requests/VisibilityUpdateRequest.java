package com.dntsystems.susu.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class VisibilityUpdateRequest {
    @Schema(description = "New visibility value", example = "VISIBLE")
    private String visible;

    public VisibilityUpdateRequest() {
    }

    public VisibilityUpdateRequest(String visible) {
        this.visible = visible;
    }

    public String getVisible() {
        return visible;
    }
    public void setVisible(String visible) {
        this.visible = visible;
    }
}
