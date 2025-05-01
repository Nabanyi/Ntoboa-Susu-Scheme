package com.dntsystems.susu.requests;

import io.swagger.v3.oas.annotations.media.Schema;

public class UpdateGroupMemberSize {
    @Schema(description = "New maximum number of members", example = "5")
    private Integer maxMember;

    public UpdateGroupMemberSize() {
    }

    public UpdateGroupMemberSize(Integer maxMember) {
        this.maxMember = maxMember;
    }

    public Integer getMaxMember() {
        return maxMember;
    }
    public void setMaxMember(Integer maxMember) {
        this.maxMember = maxMember;
    }
}
