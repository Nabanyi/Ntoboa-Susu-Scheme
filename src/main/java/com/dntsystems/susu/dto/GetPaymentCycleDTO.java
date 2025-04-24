package com.dntsystems.susu.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.List;

public class GetPaymentCycleDTO {
    private Integer id;
    private String name;
    private Double amount;
    private Double payoutAmount;
    private String payoutFrequency;
    private LocalDate startDate;
    private String status;
    private List<PaymentCycleMembers> members;

    public GetPaymentCycleDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPayoutAmount() {
        return payoutAmount;
    }

    public void setPayoutAmount(Double payoutAmount) {
        this.payoutAmount = payoutAmount;
    }

    public String getPayoutFrequency() {
        return payoutFrequency;
    }

    public void setPayoutFrequency(String payoutFrequency) {
        this.payoutFrequency = payoutFrequency;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PaymentCycleMembers> getMembers() {
        return members;
    }

    public void setMembers(List<PaymentCycleMembers> members) {
        this.members = members;
    }
}
