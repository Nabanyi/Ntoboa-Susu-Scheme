package com.dntsystems.susu.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dntsystems.susu.dto.RefreshTokenDTO;
import com.dntsystems.susu.dto.UserDTO;
import com.dntsystems.susu.entity.CustomUserDetails;
import com.dntsystems.susu.entity.User;
import com.dntsystems.susu.entity.UserStatusEnum;
import com.dntsystems.susu.exception.ResourceNotFoundException;
import com.dntsystems.susu.repository.UserRepository;
import com.dntsystems.susu.requests.AuthRequest;
import com.dntsystems.susu.requests.RegisterRequest;
import com.dntsystems.susu.requests.UpdatePassword;
import com.dntsystems.susu.requests.UserUpdateRequest;

@Service
public class AuthService {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	@Autowired
    private UserDetailsService userDetailsService;
	@Autowired
    private JwtService jwtUtil;
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	//login
    public UserDTO authenticate(AuthRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userDetails.getId());
        userDTO.setUsername(userDetails.getUsername());
        userDTO.setRole(userDetails.getRole());
        userDTO.setFirstname(userDetails.getFirstname());
        userDTO.setMiddlename(userDetails.getMiddlename());
        userDTO.setLastname(userDetails.getLastName());
        userDTO.setEmail(userDetails.getEmail());
        userDTO.setPhone(userDetails.getPhone());
        userDTO.setAddress(userDetails.getAddress());
        userDTO.setStatus(userDetails.getStatus());
        userDTO.setToken(accessToken);
        userDTO.setRefreshToken(refreshToken);

        return userDTO;
    }
    
    // Refresh access token using refresh token
    public RefreshTokenDTO refreshAccessToken(RefreshTokenDTO refreshToken) {
        if (!jwtUtil.validateRefreshToken(refreshToken.getRefreshToken())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String username = jwtUtil.extractUsername(refreshToken.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.generateAccessToken(userDetails);
        RefreshTokenDTO refreshTokenDTO2 = new RefreshTokenDTO(newAccessToken);
        return refreshTokenDTO2;
    }
    
    // User Registration
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Hash password
        user.setRole(request.getRole());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setMiddlename(request.getMiddlename());
        user.setPhone(request.getPhone());
        user.setStatus(UserStatusEnum.ACTIVE);

        userRepository.save(user);
    }
    
    // User update
    public void updateUser(UserUpdateRequest request) {
    	Integer userIdInteger = getUserId();
    	User user = userRepository.findById(userIdInteger.longValue()).orElseThrow(() -> new ResourceNotFoundException("Failed to load user with Id:"+userIdInteger));

        user.setUsername(request.getUsername());
        user.setAddress(request.getAddress());
        user.setEmail(request.getEmail());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setMiddlename(request.getMiddlename());
        user.setPhone(request.getPhone());

        userRepository.save(user);
    }
    
    // User password update
    public void updatePassword(UpdatePassword request) {
    	Integer userIdInteger = getUserId();
        User user = userRepository.findById(userIdInteger.longValue()).orElseThrow(() -> new ResourceNotFoundException("Failed to load user with Id:"+userIdInteger));

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        	throw new RuntimeException("Your current password is incorrect");
        }
        
        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
        	throw new RuntimeException("Please confirm your new password");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    
    private Integer getUserId() {
		CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userDetails.getId().intValue();
	}

}
