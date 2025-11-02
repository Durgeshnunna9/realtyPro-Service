package com.realtypro.mapper;

import com.realtypro.dto.UserDTO;
import com.realtypro.schema.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) return null;

        return new UserDTO(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getAge(),
                user.getMobile_number()
        );
    }
}

