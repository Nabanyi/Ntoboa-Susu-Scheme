package com.dntsystems.susu.utils;

import com.dntsystems.susu.entity.CustomUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class Helper {
    public Helper() {
    }

    public Integer getUserId() {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getId().intValue();
    }
}
