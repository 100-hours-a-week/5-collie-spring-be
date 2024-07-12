package com.collieReact.service;

import com.collieReact.entity.User;
import com.collieReact.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void updateNickname(Long userId, String nickname) {
        User user = getUserById(userId);
        if (user != null) {
            user.setNickname(nickname);
            userRepository.update(user);
        }
    }

    public void updatePassword(Long userId, String password) {
        User user = getUserById(userId);
        if (user != null) {
            user.setPassword(bCryptPasswordEncoder.encode(password));
            userRepository.update(user);
        }
    }

    public void updateProfilePicture(Long userId, String profilePicture) {
        User user = getUserById(userId);
        if (user != null) {
            user.setProfilePicture(profilePicture);
            userRepository.update(user);
        }
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }
}