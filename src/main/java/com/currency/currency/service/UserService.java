package com.currency.currency.service;

import com.currency.currency.dto.UserBulkDTO;
import com.currency.currency.entity.User;
import com.currency.currency.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable(value = "userCache")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "userCache", key = "#id")
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @CachePut(value = "userCache", key = "#result.id")
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "userCache", allEntries = true)
    public List<User> createBulkUsers(UserBulkDTO userBulkDTO) {
        List<User> users = userBulkDTO.getUsers().stream()
                .map(dto -> {
                    User user = new User();
                    user.setUsername(dto.getUsername());
                    return user;
                })
                .collect(Collectors.toList());
        return userRepository.saveAll(users);
    }

    @Transactional
    @CachePut(value = "userCache", key = "#id")
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setUsername(userDetails.getUsername());
        return userRepository.save(user);
    }

    @Transactional
    @CacheEvict(value = "userCache", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }
}