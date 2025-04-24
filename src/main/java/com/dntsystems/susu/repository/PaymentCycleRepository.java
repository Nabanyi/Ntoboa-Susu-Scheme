package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.PaymentCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCycleRepository extends JpaRepository<PaymentCycle, Integer> {
    boolean existsByGroupIdAndStatus(Integer groupId, String status);

    PaymentCycle findByGroupIdAndStatus(Integer groupId, String status);

    List<PaymentCycle> findAllByGroupId(Integer groupId);
}
