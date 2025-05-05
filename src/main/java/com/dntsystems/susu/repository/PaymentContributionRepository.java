package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.PaymentContribution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentContributionRepository extends JpaRepository<PaymentContribution, Integer> {
    List<PaymentContribution> findAllByCycleIdAndGroupId(Integer cycleId, Integer groupId);
}
