package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.GroupMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, Integer> {
    @Query("SELECT g FROM GroupMembership g WHERE g.groupId = ?1")
    List<GroupMembership> findByGroupId(Integer groupId);

    boolean existsByGroupIdAndUserId(Integer groupId, Integer userId);

    GroupMembership findByGroupIdAndUserId(Integer groupId, Integer userId);

    @Query("DELETE FROM GroupMembership g WHERE g.groupId = ?1 AND g.userId = ?2")
    void removeMemberFromGroup(Integer groupId, Integer userId);

    long countByGroupId(Integer groupId);

    @Query("SELECT g FROM GroupMembership g WHERE g.userId = ?1")
    List<GroupMembership> userCreatedGroups(Integer userId);
}
