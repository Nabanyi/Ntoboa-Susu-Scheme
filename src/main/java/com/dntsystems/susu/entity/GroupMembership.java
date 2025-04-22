package com.dntsystems.susu.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "group_memberships")
@IdClass(GroupMembershipId.class) // Composite primary key needs a separate class
public class GroupMembership {

    @Id
    @Column(name = "group_id")
    private Integer groupId;

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role", nullable = false, length = 15)
    private String role = "MEMBER";

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    // Constructors
    public GroupMembership() {
    }

    public GroupMembership(Integer groupId, Integer userId, String role) {
        this.groupId = groupId;
        this.userId = userId;
        this.role = role;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GroupMembership that = (GroupMembership) o;

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
