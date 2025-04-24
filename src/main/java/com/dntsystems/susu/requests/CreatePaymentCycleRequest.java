package com.dntsystems.susu.requests;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

public class CreatePaymentCycleRequest {
    @NotEmpty(message = "Policy field is required")
    private String name;
    @NotEmpty(message = "Amount field is required")
    private Double amount;
    @NotEmpty(message = "Payout Frequency field is required")
    private String payoutFrequency;
    @NotEmpty(message = "Start Date field is required")
    private LocalDate startDate;

    public CreatePaymentCycleRequest() {
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
}
