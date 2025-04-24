package com.dntsystems.susu.repository;

import com.dntsystems.susu.entity.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Integer> {
    List<PaymentOrder> findByCycleId(Integer cycleId);
}
