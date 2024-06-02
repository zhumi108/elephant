package com.tt.elephant.service;

import com.tt.elephant.repository.UserEntity;
import com.tt.elephant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired(required = false)
    private UserRepository userRepository;
    public UserEntity getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
    }
}
