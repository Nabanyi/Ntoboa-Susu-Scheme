package com.dntsystems.susu.controllers;

import com.dntsystems.susu.dto.GetPaymentCycleDTO;
import com.dntsystems.susu.dto.PaymentContributionDTO;
import com.dntsystems.susu.requests.CreatePaymentContributionDTO;
import com.dntsystems.susu.requests.CreatePaymentCycleRequest;
import com.dntsystems.susu.service.PaymentService;
import com.dntsystems.susu.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@Tag(name = "Payments", description = "APIs for managing payments, payment cycles")
public class PaymentsController {
    @Autowired
    private PaymentService paymentService;

    @Operation(summary = "Create Payment Cycle", description = "Creates a new payment cycle")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create-cycle/{groupId}")
    public ApiResponse<GetPaymentCycleDTO> createPaymentCycle(@RequestBody CreatePaymentCycleRequest request, @PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Payment cycle created successfully", paymentService.createPaymentCycle(request, groupId));
    }

    @Operation(summary = "Get current payment cycle", description = "Get current payment cycle for a group")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/current-cycle/{groupId}")
    public ApiResponse<GetPaymentCycleDTO> getCurrentPaymentCycle(@PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Data retrieved successfully", paymentService.getCurrentPaymentCycle(groupId));
    }

    @Operation(summary = "Get single payment cycle", description = "Get single payment cycle by ID")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get-cycle/{cycleId}")
    public ApiResponse<GetPaymentCycleDTO> getSinglePaymentCycle(@PathVariable Integer cycleId) {
        return new ApiResponse<>(true, "Data retrieved successfully", paymentService.getSinglePaymentCycle(cycleId));
    }

    @Operation(summary = "Get all payment cycles", description = "Get all payment cycles for a group")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/get/{groupId}")
    public ApiResponse<List<GetPaymentCycleDTO>> getAllPaymentCycles(@PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Data retrieved successfully", paymentService.getAllPaymentCycles(groupId));
    }

    @Operation(summary = "Start payment cycle", description = "Start a payment cycle for group")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cycle/{cycleId}/start")
    public ApiResponse<Void> startPaymentCycle(@PathVariable Integer cycleId) {
        paymentService.startPaymentCycle(cycleId);
        return new ApiResponse<>(true, "Payment cycle started successfully", null);
    }

    @Operation(summary = "Close payment cycle", description = "Close or end a payment cycle for a group")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cycle/{cycleId}/close")
    public ApiResponse<Void> closePaymentCycle(@PathVariable Integer cycleId) {
        paymentService.closePaymentCycle(cycleId);
        return new ApiResponse<>(true, "Payment cycle closed successfully", null);
    }

    @Operation(summary = "Get Payment Contributions", description = "Get all payment contribution for a payment cycle")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/cycle/{cycleId}/{groupId}")
    public ApiResponse<List<PaymentContributionDTO>> getPaymentCycleContribution(@PathVariable Integer cycleId, @PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Payment contributions retrieved successfully", paymentService.getPaymentCycleContribution(groupId, cycleId));
    }

    @Operation(summary = "Make Payment", description = "Make payment for a payment cycle")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/cycle/make-payment")
    public ApiResponse<Void> makeCyclePayment(@RequestBody CreatePaymentContributionDTO createPaymentContributionDTO) {
        paymentService.makePayment(createPaymentContributionDTO);
        return new ApiResponse<>(true, "Payment successfully made", null);
    }
}
