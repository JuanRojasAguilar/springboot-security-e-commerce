package com.dailycodework.dreamshops.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dailycodework.dreamshops.model.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String role);
}
