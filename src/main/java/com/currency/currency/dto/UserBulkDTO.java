package com.currency.currency.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserBulkDTO {
    private List<UserDTO> users;

    @Data
    public static class UserDTO {
        private String username;

        public UserDTO(String username) {
            this.username = username;
        }
    }
}