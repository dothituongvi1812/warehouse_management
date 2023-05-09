package com.example.warehouse_management.repository;

import com.example.warehouse_management.models.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Boolean existsByEmail(String email);
    User findTopByOrderByIdDesc();
    User findUserByEmail(String email);
    User findUserByCode(String code);
    @Query(nativeQuery = true,value = "SELECT *\n" +
            "FROM users\n" +
            "WHERE code like '%' || :keyword || '%' \n" +
            "or email LIKE '%' || :keyword || '%' \n" +
            "or full_name like '%' || :keyword || '%'")
    List<User> search(String keyword);

    List<User> findAll();

}
