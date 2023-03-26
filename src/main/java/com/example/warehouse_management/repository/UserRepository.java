package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Boolean existsByEmail(String email);
    User findTopByOrderByIdDesc();
    User findUserByEmail(String email);
    User findUserByCode(String code);

    List<User> findAll();

}
