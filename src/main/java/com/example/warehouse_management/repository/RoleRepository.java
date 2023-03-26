package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.type.ERole;
import com.example.warehouse_management.models.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
