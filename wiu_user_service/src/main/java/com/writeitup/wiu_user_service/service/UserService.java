package com.writeitup.wiu_user_service.service;

import com.writeitup.wiu_user_service.dto.UserDTO;

public interface UserService {

    UserDTO getCurrentUser();

    UserDTO update(UserDTO userDTO);
}
