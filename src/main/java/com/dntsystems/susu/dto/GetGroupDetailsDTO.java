package com.dntsystems.susu.dto;

import java.util.List;

public class GetGroupDetailsDTO {
    private GetGroupDTO group;
    private List<GetUserDTO> members;

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
}
