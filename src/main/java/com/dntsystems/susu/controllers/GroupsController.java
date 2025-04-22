package com.dntsystems.susu.controllers;

import com.dntsystems.susu.dto.GetGroupDetailsDTO;
import com.dntsystems.susu.dto.GetGroupPolicyDTO;
import com.dntsystems.susu.dto.GetUserDTO;
import com.dntsystems.susu.entity.GroupPolicy;
import com.dntsystems.susu.requests.*;
import com.dntsystems.susu.service.GroupService;
import com.dntsystems.susu.dto.GetGroupDTO;
import com.dntsystems.susu.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group")
@Tag(name = "Groups", description = "APIs for Group Management")
public class GroupsController {
    @Autowired
    private GroupService groupService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Groups", description = "Get all visible and opened groups")
    @GetMapping("/get")
    public ApiResponse<List<GetGroupDTO>> getPublicGroups() {
        return new ApiResponse<>(true, "Data retrieved successfully", groupService.getPublicGroups());
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get User Groups", description = "Get all user groups")
    @GetMapping("/get-user-groups")
    public ApiResponse<List<GetGroupDTO>> getUserGroups() {
        return new ApiResponse<>(true, "Data retrieved successfully", groupService.getUserCreatedGroups());
    }
    
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Group by ID", description = "Get single group details by group ID")
    @GetMapping("/get/{groupId}")
    public ApiResponse<GetGroupDetailsDTO> getGroupById(@PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Data retrieved successfully", groupService.getGroupById(groupId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create Group", description = "Create new group")
    @PostMapping("/create")
    public ApiResponse<GetGroupDTO> createGroup(@RequestBody CreateGroupRequest request) {
        return new ApiResponse<>(true, "Group created successfully", groupService.createGroup(request));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Group", description = "Update existing group details")
    @PostMapping("/update/{groupId}")
    public ApiResponse<GetGroupDTO> updateGroup(@RequestBody CreateGroupRequest request, @PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Group updated successfully", groupService.updateGroup(request, groupId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Group Visibility", description = "Update group visibility status")
    @PostMapping("/visibility/{groupId}")
    public ApiResponse<Void> updateGroupVisibility(@RequestBody VisibilityUpdateRequest req, @PathVariable Integer groupId) {
        groupService.updateGroupVisibility(req.getVisible(), groupId);
        return new ApiResponse<>(true, "Group visibility updated successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Group Status", description = "Update group status (OPENED/CLOSED)")
    @PostMapping("/status/{groupId}")
    public ApiResponse<Void> updateGroupStatus(@RequestBody GroupStatusUpdateRequest req, @PathVariable Integer groupId) {
        groupService.updateGroupStatus(req.getStatus(), groupId);
        return new ApiResponse<>(true, "Group status updated successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Group Member Size", description = "Update maximum number of members allowed in group")
    @PostMapping("/member-size/{groupId}")
    public ApiResponse<Void> updateGroupMemberSize(@RequestBody UpdateGroupMemberSize req, @PathVariable Integer groupId) {
        groupService.updateGroupMemberSize(req.getMaxMember(), groupId);
        return new ApiResponse<>(true, "Group member size updated successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Accept Request", description = "Accept group join request")
    @GetMapping("/accept-request/{requestId}")
    public ApiResponse<Void> acceptRequest(@PathVariable Integer requestId) {
        groupService.acceptRequest(requestId);
        return new ApiResponse<>(true, "Request accepted successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Reject Request", description = "Reject group join request")
    @GetMapping("/reject-request/{requestId}")
    public ApiResponse<Void> rejectRequest(@PathVariable Integer requestId) {
        groupService.rejectRequest(requestId);
        return new ApiResponse<>(true, "Request rejected successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Cancel Request", description = "Cancel group join request")
    @GetMapping("/cancel-request/{requestId}")
    public ApiResponse<Void> cancelRequest(@PathVariable Integer requestId) {
        groupService.cancelRequest(requestId);
        return new ApiResponse<>(true, "Request cancelled successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Make Request", description = "Make request to join group")
    @GetMapping("/make-request/{groupId}")
    public ApiResponse<Void> makeRequest(@PathVariable Integer groupId) {
        groupService.makeRequest(groupId);
        return new ApiResponse<>(true, "Request made successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Leave Group", description = "Leave group as a member")
    @GetMapping("/leave/{groupId}")
    public ApiResponse<Void> leaveGroup(@PathVariable Integer groupId) {
        groupService.leaveGroup(groupId);
        return new ApiResponse<>(true, "Left group successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove Member", description = "Remove member from group")
    @GetMapping("/remove-member/{groupId}/{userId}")
    public ApiResponse<Void> removeMember(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.removeMember(groupId, userId);
        return new ApiResponse<>(true, "Member removed successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All Sent Requests", description = "Get all requests sent by authenticated user")
    @GetMapping("/requests/sent")
    public ApiResponse<List<GetGroupDTO>> getAllRequestsSent() {
        return new ApiResponse<List<GetGroupDTO>>(true, "Requests retrieved successfully", groupService.getAllRequestsSent());
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get All Group Requests", description = "Get all requests for a specific group")
    @GetMapping("/requests/{groupId}")
    public ApiResponse<List<GetUserDTO>> getAllGroupRequests(@PathVariable Integer groupId) {
        return new ApiResponse<List<GetUserDTO>>(true, "Group requests retrieved successfully", groupService.getAllGroupRequests(groupId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Make Admin", description = "Make a user admin for a group")
    @GetMapping("/make-admin/{groupId}/{userId}")
    public ApiResponse<Void> makeAdmin(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.makeAdmin(groupId, userId);
        return new ApiResponse<>(true, "User made admin successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Remove Admin", description = "Remove admin rights from a user")
    @GetMapping("/remove-admin/{groupId}/{userId}")
    public ApiResponse<Void> removeAdmin(@PathVariable Integer groupId, @PathVariable Integer userId) {
        groupService.removeAdmin(groupId, userId);
        return new ApiResponse<>(true, "Admin rights removed successfully", null);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create Group Policy", description = "Create new policy for a group")
    @PostMapping("/policy/create/{groupId}")
    public ApiResponse<GetGroupPolicyDTO> createGroupPolicy(@RequestBody CreateGroupPolicyRequest policy, @PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Policy created successfully", groupService.createGroupPolicy(groupId, policy));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Update Group Policy", description = "Update existing group policy")
    @PostMapping("/policy/update/{policyId}")
    public ApiResponse<GetGroupPolicyDTO> updateGroupPolicy(@RequestBody CreateGroupPolicyRequest policy, @PathVariable Integer policyId) {
        return new ApiResponse<>(true, "Policy updated successfully", groupService.updateGroupPolicy(policyId, policy));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get Group Policies", description = "Get all policies for a group")
    @GetMapping("/policy/get/{groupId}")
    public ApiResponse<List<GetGroupPolicyDTO>> getGroupPolicies(@PathVariable Integer groupId) {
        return new ApiResponse<>(true, "Policies retrieved successfully", groupService.getGroupPolicies(groupId));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Delete Group Policy", description = "Delete a group policy")
    @DeleteMapping("/policy/delete/{policyId}")
    public ApiResponse<Void> deleteGroupPolicy(@PathVariable Integer policyId) {
        groupService.deleteGroupPolicy(policyId);
        return new ApiResponse<>(true, "Policy deleted successfully", null);
    }


}
