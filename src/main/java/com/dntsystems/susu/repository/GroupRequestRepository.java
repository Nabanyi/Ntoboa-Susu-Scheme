package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.GroupRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroupRequestRepository extends JpaRepository<GroupRequest, Integer> {
    boolean existsById(Integer groupId);

    List<GroupRequest> findAllByUserId(Integer userId);
    List<GroupRequest> findAllByGroupId(Integer groupId);
}
