package com.dntsystems.susu.service;

import com.dntsystems.susu.dto.*;
import com.dntsystems.susu.entity.*;
import com.dntsystems.susu.repository.*;
import com.dntsystems.susu.requests.CreateGroupPolicyRequest;
import com.dntsystems.susu.requests.CreateGroupRequest;
import com.dntsystems.susu.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private static final Logger log = LoggerFactory.getLogger(GroupService.class);
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private GroupMembershipRepository groupMembershipRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Helper helper;
    @Autowired
    private GroupRequestRepository groupRequestRepository;
    @Autowired
    private GroupPolicyRepository groupPolicyRepository;

    public List<GetGroupDTO> getPublicGroups() {
        List<Group> groupList = groupRepository.findByVisibility("VISIBLE");

        List<Long> userIds = new ArrayList<>();
        groupList.forEach((group) -> {
            userIds.add(Long.parseLong(group.getCreatedBy()));
        });

        List<User> users = userRepository.findByIdIn(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<GroupMembership> userMemberList = groupMembershipRepository.userCreatedGroups(helper.getUserId());
        List<Integer> groupIds = new ArrayList<>();
        userMemberList.forEach((group) -> {
            groupIds.add(group.getGroupId());
        });

        return groupList.stream()
                .filter(group -> !groupIds.contains(group.getId()))
                .map((group) -> {
            GetGroupDTO dto = new GetGroupDTO();
            BeanUtils.copyProperties(group, dto);
            User user = userMap.get(Long.parseLong(group.getCreatedBy()));
            dto.setCreatedByName(user.getFirstname() + " " + user.getLastname() + " " + user.getMiddlename());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<GetGroupDTO> getUserCreatedGroups() {
        List<GroupMembership> userMemberList = groupMembershipRepository.userCreatedGroups(helper.getUserId());
        List<Integer> groupIds = new ArrayList<>();
        userMemberList.forEach((group) -> {
            groupIds.add(group.getGroupId());
        });

        List<Group> groupList = groupRepository.findByIdIn(groupIds);

        List<Long> userIds = new ArrayList<>();
        groupList.forEach((group) -> {
            userIds.add(Long.parseLong(group.getCreatedBy()));
        });

        List<User> users = userRepository.findByIdIn(userIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        return groupList.stream().map((group) -> {
            GetGroupDTO dto = new GetGroupDTO();
            BeanUtils.copyProperties(group, dto);
            User user = userMap.get(Long.parseLong(group.getCreatedBy()));
            dto.setCreatedByName(user.getFirstname() + " " + user.getLastname() + " " + user.getMiddlename());
            return dto;
        }).collect(Collectors.toList());
    }

    public GetGroupDetailsDTO getGroupById(Integer groupId) {
        //get group details
        Group group = groupRepository.findSingleGroup(groupId);
        GetGroupDTO groupDTO = new GetGroupDTO();
        BeanUtils.copyProperties(group, groupDTO);

        //get group members
        List<GroupMembership> members = groupMembershipRepository.findByGroupId(group.getId());

        //get only members ids
        List<Long> memberIds = new ArrayList<>();
        members.forEach((member) -> {
            memberIds.add(member.getUserId().longValue());
        });

        //get member details
        List<User> users = userRepository.findByIdIn(memberIds);
        Map<Long, User> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        //map member's ids to member's details
        List<GetUserDTO> userDTOList = members.stream().map((member) -> {
            GetUserDTO dto = new GetUserDTO();
            BeanUtils.copyProperties(userMap.get(member.getUserId().longValue()), dto);
            dto.setIsAdmin(member.getRole());
            dto.setJoinedAt(member.getJoinedAt());
            return dto;
        }).collect(Collectors.toList());

        User createdBy = userMap.get(Long.parseLong(group.getCreatedBy()));
        groupDTO.setCreatedByName(createdBy.getFirstname() + " " + createdBy.getLastname() + " " + createdBy.getMiddlename());

        //create response data
        GetGroupDetailsDTO groupDetailsDTO = new GetGroupDetailsDTO();
        groupDetailsDTO.setGroup(groupDTO);
        groupDetailsDTO.setMembers(userDTOList);

        //get group policies
        List<GroupPolicy> policies = groupPolicyRepository.findAllByGroupId(groupId);
        List<GetGroupPolicyDTO> policyDTOS = policies.stream().map((policy) -> {
                    GetGroupPolicyDTO dto = new GetGroupPolicyDTO();
                    BeanUtils.copyProperties(policy, dto);
                    return dto;
                }).sorted((a, b) -> a.getPriority().compareTo(b.getPriority()))
                .collect(Collectors.toList());

        groupDetailsDTO.setPolicies(policyDTOS);
        return groupDetailsDTO;
    }

    //create group and make user an admin
    public GetGroupDTO createGroup(CreateGroupRequest group) {
        if (groupRepository.existsByName(group.getName())) {
            throw new RuntimeException("Group name already taken!");
        }


        Group newGroup = new Group();
        newGroup.setName(group.getName());
        newGroup.setDescription(group.getDescription());
        newGroup.setCreatedBy(helper.getUserId().toString());

        Group createdGroup = groupRepository.save(newGroup);
        GetGroupDTO groupDTO = new GetGroupDTO();
        BeanUtils.copyProperties(createdGroup, groupDTO);

        GroupMembership membership = new GroupMembership();
        membership.setGroupId(createdGroup.getId());
        membership.setUserId(helper.getUserId());
        membership.setRole("ADMIN");
        groupMembershipRepository.save(membership);

        return groupDTO;
    }

    //update group details
    public GetGroupDTO updateGroup(CreateGroupRequest request, Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");
        if (groupRepository.findByNameAndIdNot(request.getName(), groupId) != null)  throw new RuntimeException("Group name is already taken!");

        Group savedGroup = groupRepository.findSingleGroup(groupId);
        savedGroup.setName(request.getName());
        savedGroup.setDescription(request.getDescription());
        savedGroup.setUpdatedBy(helper.getUserId().toString());
        savedGroup.setId(groupId);
        savedGroup.setUpdatedAt(LocalDateTime.now());

        Group createdGroup = groupRepository.save(savedGroup);
        GetGroupDTO groupDTO = new GetGroupDTO();
        BeanUtils.copyProperties(createdGroup, groupDTO);
        return groupDTO;
    }

    //update group visibility
    public void updateGroupVisibility(String request, Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");

        Group newGroup = groupRepository.findSingleGroup(groupId);
        newGroup.setVisibility("VISIBLE".equals(request) ? "VISIBLE" : "INVISIBLE");
        newGroup.setUpdatedBy(helper.getUserId().toString());

        groupRepository.save(newGroup);
    }

    //update group status
    public void updateGroupStatus(String request, Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");

        Group newGroup = groupRepository.findSingleGroup(groupId);
        newGroup.setStatus("OPENED".equals(request) ? "OPENED" : "CLOSED");
        newGroup.setUpdatedBy(helper.getUserId().toString());
        newGroup.setId(groupId);

        groupRepository.save(newGroup);
    }

    //update group member-size
    public void updateGroupMemberSize(Integer max_member, Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");

        Group updateGroup = groupRepository.findSingleGroup(groupId);
        updateGroup.setMemberSize(max_member);
        updateGroup.setUpdatedBy(helper.getUserId().toString());
        updateGroup.setUpdatedAt(LocalDateTime.now());
        updateGroup.setId(groupId);

        groupRepository.save(updateGroup);
    }

    //accept user group join request
    //this function saves user to membership table and delete from request table
    public void acceptRequest(Integer groupRequestId) {
        GroupRequest groupRequest = groupRequestRepository.findById(groupRequestId).orElseThrow(() -> new RuntimeException("Request details not found!"));
        Integer groupId = groupRequest.getGroupId();
        Integer userId = groupRequest.getUserId();

        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        if (groupMembershipRepository.existsByGroupIdAndUserId(groupId, userId))  throw new RuntimeException("User already a member of this group!");

        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group details not found!"));
        long groupMembersCount = groupMembershipRepository.countByGroupId(groupId);
        if (groupMembersCount  >= group.getMemberSize()) {
            throw new RuntimeException("Sorry this group is full! Please try again later or contact the group admin to increase the group member size.");
        }

        GroupMembership membership = new GroupMembership();
        membership.setGroupId(groupId);
        membership.setUserId(userId);
        membership.setRole("MEMBER");
        groupMembershipRepository.save(membership);
        groupRequestRepository.deleteById(groupRequestId);
    }

    //reject user group join request
    //this function deletes user from request table by admin
    public void rejectRequest(Integer groupRequestId) {
        GroupRequest groupRequest = groupRequestRepository.findById(groupRequestId).orElseThrow(() -> new RuntimeException("Request details not found!"));
        Integer groupId = groupRequest.getGroupId();
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        groupRequestRepository.deleteById(groupRequestId);
    }

    //cancel user group join request
    //this function deletes user from request table by the user who applied to join
    public void cancelRequest(Integer groupRequestId) {
        GroupRequest groupRequest = groupRequestRepository.findById(groupRequestId).orElseThrow(() -> new RuntimeException("Request details not found!"));
        if(!groupRequest.getUserId().equals(helper.getUserId()))  throw new RuntimeException("Sorry you are not authorized to cancel this request!");
        groupRequestRepository.deleteById(groupRequestId);
    }

    //allows users to make request to join a group
    //this function saves user to group request table
    public void makeRequest(Integer groupId) {
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");
        if (!userRepository.existsById(helper.getUserId()))  throw new RuntimeException("User details not found!");
        if (groupMembershipRepository.existsByGroupIdAndUserId(groupId, helper.getUserId()))  throw new RuntimeException("User already a member of this group!");

        Group group = groupRepository.findSingleGroup(groupId);
        if (group.getStatus().equals("CLOSED"))  throw new RuntimeException("Sorry this group is closed! Please try again later or contact the group admin to open the group.");

        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setGroupId(groupId);
        groupRequest.setUserId(helper.getUserId());
        groupRequestRepository.save(groupRequest);
    }

    //allows users to leave a group
    //this function deletes user from group membership table
    public void leaveGroup(Integer groupId) {
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");
        if (!groupMembershipRepository.existsByGroupIdAndUserId(groupId, helper.getUserId()))  throw new RuntimeException("User is not a member of this group!");
        groupMembershipRepository.removeMemberFromGroup(groupId, helper.getUserId());

        long groupMembersCount = groupMembershipRepository.countByGroupId(groupId);
        if (groupMembersCount == 0) {
            groupRepository.deleteById(groupId);
        }
    }

    //allows users to remove other users from a group
    //this function deletes user from group membership table
    public void removeMember(Integer groupId, Integer userId) {
        if (!groupRepository.existsById(groupId))  throw new RuntimeException("Group details not found!");
        if (!groupMembershipRepository.existsByGroupIdAndUserId(groupId, userId))  throw new RuntimeException("User is not a member of the group selected!");
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))  throw new RuntimeException("You are not an admin of this group!");
        groupMembershipRepository.removeMemberFromGroup(groupId, userId);
    }

    //get all group requests sent by the user
    public List<GetGroupDTO> getAllRequestsSent() {
        List<GroupRequest> requests = groupRequestRepository.findAllByUserId(helper.getUserId());
        List<Integer> groupIds = requests.stream()
                .map(GroupRequest::getGroupId)
                .collect(Collectors.toList());

        List<Group> groups = groupRepository.findAllById(groupIds);
        return groups.stream().map(group -> {
            GetGroupDTO dto = new GetGroupDTO();
            BeanUtils.copyProperties(group, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    //get all join requests sent by user and received as group admin
    public List<GetUserDTO> getAllGroupRequests(Integer groupId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        List<GroupRequest> requests = groupRequestRepository.findAllByGroupId(groupId);
        List<Long> userIds = requests.stream()
                .map(GroupRequest::getUserId)
                .map(Integer::longValue)
                .collect(Collectors.toList());

        List<User> users = userRepository.findAllById(userIds);
        Map<Long, User> userLists = users.stream().collect(Collectors.toMap(User::getId, user -> user));

        return requests.stream().map(request -> {
            User user = userLists.get(request.getUserId().longValue());
            GetUserDTO dto = new GetUserDTO();
            dto.setId(request.getId().longValue());
            dto.setJoinedAt(request.getRequestedAt());
            dto.setFirstname(user.getFirstname());
            dto.setLastname(user.getLastname());
            dto.setMiddlename(user.getMiddlename());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setAddress(user.getAddress());
            dto.setIsAdmin(null);
            return dto;
        }).collect(Collectors.toList());
    }

    //allows users to make other users admins of a group
    //this function updates user's role to admin in group membership table
    public void makeAdmin(Integer groupId, Integer userId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))
            throw new RuntimeException("Group details not found!");
        if (!groupMembershipRepository.existsByGroupIdAndUserId(groupId, userId))
            throw new RuntimeException("User is not a member of this group!");

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndUserId(groupId, userId);
        membership.setRole("ADMIN");
        groupMembershipRepository.save(membership);
    }

    //allows users to remove other users admins of a group
    //this function updates user's role to member in group membership table
    public void removeAdmin(Integer groupId, Integer userId) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");
        if (!groupRepository.existsById(groupId))
            throw new RuntimeException("Group details not found!");
        if (!groupMembershipRepository.existsByGroupIdAndUserId(groupId, userId))
            throw new RuntimeException("User is not a member of this group!");

        GroupMembership membership = groupMembershipRepository.findByGroupIdAndUserId(groupId, userId);
        membership.setRole("MEMBER");
        groupMembershipRepository.save(membership);
    }

    private String getGroupRole(Integer groupId, Integer userId) {
        GroupMembership member = groupMembershipRepository.findByGroupIdAndUserId(groupId, userId);
        if (member == null)  throw new RuntimeException("User is not a member of this group!");
        return member.getRole();
    }

    public GetGroupPolicyDTO createGroupPolicy(Integer groupId, CreateGroupPolicyRequest request) {
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        GroupPolicy policy = new GroupPolicy();
        policy.setGroupId(groupId);
        policy.setPolicy(request.getPolicy());
        if (request.getPriority() == null){
            long policyCount = groupPolicyRepository.countByGroupId(groupId);
            policy.setPriority((int) policyCount + 1);
        }else{
            policy.setPriority(request.getPriority());
        }
        policy.setCreatedBy(helper.getUserId());
        policy.setCreatedAt(LocalDateTime.now());

        GroupPolicy createdPolicy = groupPolicyRepository.save(policy);
        GetGroupPolicyDTO dto = new GetGroupPolicyDTO();
        BeanUtils.copyProperties(createdPolicy, dto);
        return dto;
    }

    public GetGroupPolicyDTO updateGroupPolicy(Integer policyId, CreateGroupPolicyRequest request) {
        GroupPolicy policy = groupPolicyRepository.findById(policyId).orElseThrow(() -> new RuntimeException("Group details not found!"));
        Integer groupId = policy.getGroupId();
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        policy.setPolicy(request.getPolicy());
        if (request.getPriority() != null){
            policy.setPriority(request.getPriority());
        }
        policy.setUpdatedBy(helper.getUserId());
        policy.setUpdatedAt(LocalDateTime.now());

        GroupPolicy createdPolicy = groupPolicyRepository.save(policy);
        GetGroupPolicyDTO dto = new GetGroupPolicyDTO();
        BeanUtils.copyProperties(createdPolicy, dto);
        return dto;
    }

    public List<GetGroupPolicyDTO> getGroupPolicies(Integer groupId) {
        List<GroupPolicy> policies = groupPolicyRepository.findAllByGroupId(groupId);
        return policies.stream().map((policy) -> {
                    GetGroupPolicyDTO dto = new GetGroupPolicyDTO();
                    BeanUtils.copyProperties(policy, dto);
                    return dto;
                }).sorted((a, b) -> a.getPriority().compareTo(b.getPriority()))
                .collect(Collectors.toList());
    }

    public void deleteGroupPolicy(Integer policyId) {
        GroupPolicy policy = groupPolicyRepository.findById(policyId)
                .orElseThrow(() -> new RuntimeException("Policy not found!"));
        Integer groupId = policy.getGroupId();
        if (!getGroupRole(groupId, helper.getUserId()).equals("ADMIN"))
            throw new RuntimeException("You are not an admin of this group!");

        groupPolicyRepository.deleteById(policyId);
    }
}
