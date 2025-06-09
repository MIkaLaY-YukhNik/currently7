package com.currency.currency.service;

import com.currency.currency.dto.UserBulkDTO;
import com.currency.currency.entity.User;
import com.currency.currency.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private User user1;

    @Mock
    private User user2;

    @BeforeEach
    void setUp() {
        when(user1.getId()).thenReturn(1L);
        when(user1.getUsername()).thenReturn("user1");

        when(user2.getId()).thenReturn(2L);
        when(user2.getUsername()).thenReturn("user2");
    }

    @Test
    void shouldReturnAllUsersWhenRequested() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        List<User> result = userService.getAllUsers();
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserWhenIdExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        Optional<User> result = userService.getUserById(1L);
        assertTrue(result.isPresent());
        assertEquals(user1, result.get());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenUserIdNotExists() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById(999L);
        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void shouldSaveAndReturnUserWhenCreated() {
        when(userRepository.save(user1)).thenReturn(user1);
        User result = userService.createUser(user1);
        assertEquals(user1, result);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void shouldSaveAllUsersWhenBulkCreated() {
        UserBulkDTO bulkDTO = mock(UserBulkDTO.class);
        UserBulkDTO.UserDTO dto1 = mock(UserBulkDTO.UserDTO.class);
        UserBulkDTO.UserDTO dto2 = mock(UserBulkDTO.UserDTO.class);
        when(bulkDTO.getUsers()).thenReturn(Arrays.asList(dto1, dto2));
        when(dto1.getUsername()).thenReturn("user1");
        when(dto2.getUsername()).thenReturn("user2");
        when(userRepository.saveAll(anyList())).thenReturn(Arrays.asList(user1, user2));
        List<User> result = userService.createBulkUsers(bulkDTO);
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldHandleEmptyListWhenBulkCreated() {
        UserBulkDTO bulkDTO = mock(UserBulkDTO.class);
        when(bulkDTO.getUsers()).thenReturn(Collections.emptyList());
        when(userRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        List<User> result = userService.createBulkUsers(bulkDTO);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldHandleNullListWhenBulkCreated() {
        UserBulkDTO bulkDTO = mock(UserBulkDTO.class);
        when(bulkDTO.getUsers()).thenReturn(null);
        when(userRepository.saveAll(anyList())).thenReturn(Collections.emptyList());
        List<User> result = userService.createBulkUsers(bulkDTO);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldUpdateAndReturnUserWhenIdExists() {
        User updatedUser = mock(User.class);
        when(updatedUser.getId()).thenReturn(1L);
        when(updatedUser.getUsername()).thenReturn("updated_user");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.save(user1)).thenReturn(user1);
        when(user1.getUsername()).thenReturn("updated_user");
        User result = userService.updateUser(1L, updatedUser);
        assertEquals("updated_user", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForUpdate() {
        User newUser = mock(User.class);
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.updateUser(999L, newUser));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldDeleteUserWhenIdExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        userService.deleteUser(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).delete(user1);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundForDelete() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).delete(any());
    }
}