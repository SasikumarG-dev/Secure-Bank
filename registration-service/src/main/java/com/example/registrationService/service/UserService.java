package com.example.registrationService.service;

import com.example.registrationService.dto.GetUserDetailDto;
import com.example.registrationService.dto.UserDto;
import com.example.registrationService.entity.User;
import com.example.registrationService.exception.UserNotFoundException;


public interface UserService {
    String registerUser(UserDto userDto);
    User getUserDetailsById(GetUserDetailDto getUserDetailDto) throws UserNotFoundException;
}

