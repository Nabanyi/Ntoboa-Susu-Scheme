package com.dntsystems.susu.service;

import com.dntsystems.susu.dto.GetPaymentCycleDTO;
import com.dntsystems.susu.dto.PaymentCycleMembers;
import com.dntsystems.susu.entity.*;
import com.dntsystems.susu.repository.GroupMembershipRepository;
import com.dntsystems.susu.repository.PaymentCycleRepository;
import com.dntsystems.susu.repository.PaymentOrderRepository;
import com.dntsystems.susu.repository.UserRepository;
import com.dntsystems.susu.requests.CreatePaymentCycleRequest;
import com.dntsystems.susu.utils.Helper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentCycleRepository paymentCycleRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    @Autowired
    private PaymentOrderRepository paymentOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Helper helper;

    private LocalDate calculateStartDateInDays(Integer userPosition, String frequency, LocalDate startDate){
        Integer startDateInDays = switch (frequency) {
            case "WEEKLY" -> userPosition * 7;
            case "MONTHLY" -> userPosition * 30;
            default -> 0;
        };
        return startDate.plusDays(startDateInDays);
    }

    private String getGroupRole(Integer groupId, Integer userId) {
        GroupMembership member = groupMembershipRepository.findByGroupIdAndUserId(groupId, userId);
        if (member == null)  throw new RuntimeException("User is not a member of this group!");
        return member.getRole();
    }

    //create cycle for a group by an admin
    //calculates payout amount for each member of the group
    public GetPaymentCycleDTO createPaymentCycle(CreatePaymentCycleRequest request, Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        if(paymentCycleRepository.existsByGroupIdAndStatus(groupId,"ACTIVE"))
            throw new RuntimeException("There is already an active payment cycle!");

        PaymentCycle cycle = new PaymentCycle();
        BeanUtils.copyProperties(request, cycle);
        cycle.setCreatedBy(helper.getUserId());
        cycle.setGroupId(groupId);
        cycle.setStatus("ACTIVE");

        long groupMembersCount = groupMembershipRepository.countByGroupId(groupId);
        Double payoutAmount = (groupMembersCount - 1) * request.getAmount();
        cycle.setPayoutAmount(payoutAmount);

        PaymentCycle createdCycle = paymentCycleRepository.save(cycle);
        GetPaymentCycleDTO dto = new GetPaymentCycleDTO();
        BeanUtils.copyProperties(createdCycle, dto);
        
        List<PaymentOrder> membersOrders = new ArrayList<>();
        List<PaymentCycleMembers> cycleMembers = new ArrayList<>();

        List<GroupMembership> members = groupMembershipRepository.findByGroupId(groupId);
        List<Long> userIds = members.stream()
                .map(GroupMembership::getUserId)
                .map(Integer::longValue)
                .toList();
        List<User> users = userRepository.findAllById(userIds);
        Map<Long, User> userLists = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        for (int i = 0; i < members.size(); i++) {
            GroupMembership member = members.get(i);
            PaymentOrder order = new PaymentOrder();
            order.setCycleId(createdCycle.getId());
            order.setGroupId(groupId);
            order.setMemberId(member.getUserId());
            order.setPriority(i + 1);
            order.setStatus("PENDING");

            LocalDate memberStartDate = calculateStartDateInDays(i, createdCycle.getPayoutFrequency(), createdCycle.getStartDate());
            order.setStartDate(memberStartDate);

            LocalDate memberEndDate = (Objects.equals(createdCycle.getPayoutFrequency(), "WEEKLY")) ? memberStartDate.plusWeeks(1) : memberStartDate.plusMonths(1);
            order.setEndDate(memberEndDate);

            membersOrders.add(order);

            User user = userLists.get(member.getUserId().longValue());
            PaymentCycleMembers cycleMembers1 = getPaymentCycleMembers(order, user);
            cycleMembers.add(cycleMembers1);
        }

        paymentOrderRepository.saveAll(membersOrders);
        dto.setMembers(cycleMembers);
        return dto;
    }

    public GetPaymentCycleDTO getCurrentPaymentCycle(Integer groupId) {
        PaymentCycle cycle = paymentCycleRepository.findByGroupIdAndStatus(groupId, "ACTIVE");
        if (cycle == null) {
            throw new RuntimeException("No active payment cycle found for this group!");
        }

        GetPaymentCycleDTO dto = new GetPaymentCycleDTO();
        BeanUtils.copyProperties(cycle, dto);

        List<PaymentOrder> orders = paymentOrderRepository.findByCycleId(cycle.getId());

        List<Long> userIds = orders.stream()
                .map(PaymentOrder::getMemberId)
                .map(Integer::longValue)
                .toList();
        List<User> users = userRepository.findAllById(userIds);
        Map<Long, User> userLists = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        List<PaymentCycleMembers> cycleMembers = new ArrayList<>();
        for (PaymentOrder order : orders) {
            User user = userLists.get(order.getMemberId().longValue());
            PaymentCycleMembers cycleMember = getPaymentCycleMembers(order, user);
            cycleMembers.add(cycleMember);
        }

        dto.setMembers(cycleMembers);
        return dto;
    }

    public GetPaymentCycleDTO getSinglePaymentCycle(Integer cycleId) {
        PaymentCycle cycle = paymentCycleRepository.findById(cycleId).orElseThrow(() -> new RuntimeException("Payment cycle details not found!"));

        GetPaymentCycleDTO dto = new GetPaymentCycleDTO();
        BeanUtils.copyProperties(cycle, dto);

        List<PaymentOrder> orders = paymentOrderRepository.findByCycleId(cycle.getId());

        List<Long> userIds = orders.stream()
                .map(PaymentOrder::getMemberId)
                .map(Integer::longValue)
                .toList();
        List<User> users = userRepository.findAllById(userIds);
        Map<Long, User> userLists = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        List<PaymentCycleMembers> cycleMembers = new ArrayList<>();
        for (PaymentOrder order : orders) {
            User user = userLists.get(order.getMemberId().longValue());
            PaymentCycleMembers cycleMember = getPaymentCycleMembers(order, user);
            cycleMembers.add(cycleMember);
        }

        dto.setMembers(cycleMembers);
        return dto;
    }

    public List<GetPaymentCycleDTO> getAllPaymentCycles(Integer groupId) {
        List<PaymentCycle> cycles = paymentCycleRepository.findAllByGroupId(groupId);
        List<GetPaymentCycleDTO> results = new ArrayList<>();

        for (PaymentCycle cycle : cycles) {
            GetPaymentCycleDTO dto = new GetPaymentCycleDTO();
            BeanUtils.copyProperties(cycle, dto);

            List<PaymentOrder> orders = paymentOrderRepository.findByCycleId(cycle.getId());

            List<Long> userIds = orders.stream()
                    .map(PaymentOrder::getMemberId)
                    .map(Integer::longValue)
                    .toList();
            List<User> users = userRepository.findAllById(userIds);
            Map<Long, User> userLists = users.stream().collect(Collectors.toMap(User::getId, user -> user));

            List<PaymentCycleMembers> cycleMembers = new ArrayList<>();
            for (PaymentOrder order : orders) {
                User user = userLists.get(order.getMemberId().longValue());
                PaymentCycleMembers cycleMember = getPaymentCycleMembers(order, user);
                cycleMembers.add(cycleMember);
            }

            dto.setMembers(cycleMembers);

            results.add(dto);
        }

        return results;
    }

    public void startPaymentCycle(Integer cycleId) {
        PaymentCycle cycle = paymentCycleRepository.findById(cycleId).orElseThrow(() -> new RuntimeException("Payment cycle details not found!"));

        if (!getGroupRole(cycle.getGroupId(), helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        cycle.setStatus("IN-PROGRESS");
        paymentCycleRepository.save(cycle);
    }

    public void closePaymentCycle(Integer cycleId) {
        PaymentCycle cycle = paymentCycleRepository.findById(cycleId).orElseThrow(() -> new RuntimeException("Payment cycle details not found!"));

        if (!getGroupRole(cycle.getGroupId(), helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        cycle.setStatus("COMPLETED");
        paymentCycleRepository.save(cycle);
    }

    private static PaymentCycleMembers getPaymentCycleMembers(PaymentOrder order, User user) {
        PaymentCycleMembers cycleMember = new PaymentCycleMembers();
        cycleMember.setFirstName(user.getFirstname());
        cycleMember.setLastName(user.getLastname());
        cycleMember.setMiddleName(user.getMiddlename());
        cycleMember.setEndDate(order.getEndDate());
        cycleMember.setStartDate(order.getStartDate());
        cycleMember.setPriority(order.getPriority());
        cycleMember.setMemberId(order.getMemberId());
        cycleMember.setStatus(order.getStatus());
        return cycleMember;
    }

}
