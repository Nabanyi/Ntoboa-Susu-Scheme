package com.dntsystems.susu.dto;

public class GetGroupPolicyDTO {
    private Integer id;
    private Integer priority;
    private String policy;

    public GetGroupPolicyDTO() {
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getPriority() {
        return priority;
    }
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    public String getPolicy() {
        return policy;
    }
    public void setPolicy(String policy) {
        this.policy = policy;
    }
}
