package com.dntsystems.susu.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateGroupMemberSize {
    @Schema(description = "New maximum number of members", example = "5")
    private String maxMember;

    public UpdateGroupMemberSize() {
    }

    public UpdateGroupMemberSize(String maxMember) {
        this.maxMember = maxMember;
    }

    public String getMaxMember() {
        return maxMember;
    }
    public void setMaxMember(String maxMember) {}
}
