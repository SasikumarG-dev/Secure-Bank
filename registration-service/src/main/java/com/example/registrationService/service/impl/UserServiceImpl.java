package com.example.registrationService.service.impl;




import com.example.registrationService.dto.GetUserDetailDto;
import com.example.registrationService.dto.UserDto;
import com.example.registrationService.entity.User;
import com.example.registrationService.exception.UserNotFoundException;
import com.example.registrationService.repository.UserRepository;
import com.example.registrationService.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    @NonNull
    private final UserRepository userRepository;
    @NonNull
    private final PasswordEncoder passwordEncoder;
    @NonNull
    private final ModelMapper modelMapper;
    public String registerUser(@Valid UserDto userDto)  {
        User user = modelMapper.map(userDto,User.class);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedName(user.getEmailId());
        user.setCreatedDate(LocalDate.now());
        userRepository.save(user);
        return "User Added";
    }


    public void initAdmin(){
        User user = new User();
        user.setLastName("ADMIN");
        user.setPassword(passwordEncoder.encode("Admin"));
        user.setEmailId("admin@gmail.com");
        user.setAddress("Chennai");
        user.setRole("ADMIN");
        user.setPhoneNumber("1234567890");
        user.setPanNumber("ADMIN1234A");
        user.setDateOfBirth("12/10/2000");
        user.setGender("Male");
        user.setCity("Chennai");
        user.setStreet("adstreet");
        user.setPincode("123456");
        user.setCreatedDate(LocalDate.now());
        user.setCreatedName("Admin");
        userRepository.save(user);
    }

    @Override
    public User getUserDetailsById(GetUserDetailDto getUserDetailDto) throws UserNotFoundException {
        return userRepository.findById(getUserDetailDto.getUserId()).get();
    }

}