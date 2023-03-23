package com.example.warehouse_management.security.services;

import com.example.warehouse_management.exception.NotFoundGlobalException;
import com.example.warehouse_management.models.user.User;
import com.example.warehouse_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if(user==null){
            throw new NotFoundGlobalException("Không tìm thấy user");
        }
        return UserDetailsImpl.build(user);
    }
}
