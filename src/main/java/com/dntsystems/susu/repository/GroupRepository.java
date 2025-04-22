package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    @Query("SELECT g FROM Group g WHERE g.visibility = ?1")
    List<Group> findByVisibility(String visibility);

    @Query("SELECT g FROM Group g WHERE g.id = ?1")
    Group findSingleGroup(Integer groupId);

    boolean existsById(Integer groupId);

    boolean existsByName(String name);

    boolean existsByIdAndName(Integer groupId, String name);

    List<Group> findByIdIn(List<Integer> groupIds);
}
