package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.GroupPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupPolicyRepository extends JpaRepository<GroupPolicy, Integer> {
    List<GroupPolicy> findAllByGroupId(Integer groupId);

    long countByGroupId(Integer groupId);
}
