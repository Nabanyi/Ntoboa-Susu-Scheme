package com.dntsystems.susu.requests;

import jakarta.validation.constraints.NotEmpty;

public class CreateGroupPolicyRequest {
    @NotEmpty(message = "Policy field is required")
    private String policy;

    private Integer priority;

    public CreateGroupPolicyRequest() {
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }
}
