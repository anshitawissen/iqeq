package com.iqeq.service;

import com.iqeq.dto.common.UserDto;
import com.iqeq.model.User;
import com.iqeq.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
	
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
	
    public UserDto getUserByEmail(String email) {
        Optional<User> userOptional = userRepository.findByWorkEmail(email);
        return userOptional.map(UserDto::build).orElse(null);
    }


}
