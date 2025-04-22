package com.dntsystems.susu.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GroupMembershipId implements Serializable {

    private Integer groupId;

    private Integer userId;

    // Constructors
    public GroupMembershipId() {
    }

    public GroupMembershipId(Integer groupId, Integer userId) {
        this.groupId = groupId;
        this.userId = userId;
    }

    // Getters and Setters
    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembershipId that = (GroupMembershipId) o;

        if (!groupId.equals(that.groupId)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + userId.hashCode();
        return result;
    }
}
