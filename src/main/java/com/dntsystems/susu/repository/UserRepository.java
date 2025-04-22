package com.dntsystems.susu.repository;

import java.util.List;
import java.util.Optional;

import com.dntsystems.susu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String username);
	boolean existsById(Integer userId);
	
	List<User> findByIdIn(List<Long> userIds);
}
