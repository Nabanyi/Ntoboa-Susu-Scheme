package com.dntsystems.susu.dto;

import java.util.List;

public class GetGroupDetailsDTO {
    private GetGroupDTO group;
    private List<GetUserDTO> members;
    private List<GetGroupPolicyDTO> policies;

    public GetGroupDetailsDTO() {
    }

    public GetGroupDTO getGroup() {
        return group;
    }
    public void setGroup(GetGroupDTO group) {
        this.group = group;
    }

    public List<GetUserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<GetUserDTO> members) {
        this.members = members;
    }

    public List<GetGroupPolicyDTO> getPolicies() {
        return policies;
    }

    public void setPolicies(List<GetGroupPolicyDTO> policies) {
        this.policies = policies;
    }
}
