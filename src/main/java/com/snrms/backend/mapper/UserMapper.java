package com.snrms.backend.mapper;

import com.snrms.backend.dto.UserDTO;
import com.snrms.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) return null;
        return new UserDTO(user.getUserID(), user.getFullName(), user.getUsername(),
                user.getEmail(), user.getRole(), user.isActive());
    }
}
