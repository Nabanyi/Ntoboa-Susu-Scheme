package com.dntsystems.susu.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public class CreatePaymentContributionDTO {

    @JsonProperty("cycle_id")
    @NotEmpty(message = "Cycle Id is required")
    private Integer cycleId;
    @JsonProperty("group_id")
    @NotEmpty(message = "Group Id is required")
    private Integer groupId;
    @NotEmpty(message = "Amount is required")
    private Double amount;
    @JsonProperty("member_id")
    @NotEmpty(message = "Member Id is required")
    private Integer memberId;

    public CreatePaymentContributionDTO() {
    }

    public Integer getCycleId() {
        return cycleId;
    }

    public void setCycleId(Integer cycleId) {
        this.cycleId = cycleId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }
}
