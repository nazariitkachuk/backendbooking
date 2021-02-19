package com.hotelapp.services;

import com.hotelapp.daos.RolesDto;
import com.hotelapp.daos.UserDto;
import com.hotelapp.entities.Role;
import com.hotelapp.entities.User;
import com.hotelapp.exceptions.DataDuplicateExceptoion;
import com.hotelapp.exceptions.DataNotFoundException;
import com.hotelapp.exceptions.OverrideNotAllowedException;
import com.hotelapp.repository.RoleRepository;
import com.hotelapp.repository.UserRepository;
import com.hotelapp.utils.CustomBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Arrays;

import static com.hotelapp.configuration.security.constants.RoleNames.ROLE_USER;


@Service
public class UserService {


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Transactional
    public UserDto createUser(UserDto userDto) {

        Role role = roleRepository.findByRoleName(ROLE_USER)
                .orElseThrow(() -> new DataNotFoundException("Role "+ROLE_USER+"does not exist in db"));

        User userEntity = new User();
        userEntity.setIsActive(true);
        UserDto userDtoCreated = new UserDto();
        BeanUtils.copyProperties(userDto, userEntity, "id", "password");

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new DataDuplicateExceptoion("username", userDto.getUsername());
        }

        userEntity.setRoles(Arrays.asList(role));


        User createdUser = userRepository.save(userEntity);
        BeanUtils.copyProperties(createdUser, userDtoCreated);
        createdUser.setPassword(passwordEncoder.encode(userDto.getPassword()));

         return userDtoCreated;
    }

    @Transactional
    public UserDto updateOtherUser(UserDto dto, Long userId, String userToSkip) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id: "+userId+" does not exist"));

        if (userToSkip.equals(user.getUsername())) {
            throw new OverrideNotAllowedException("It is not allowed to update user by this endpoint");
        }

        return updateUser(dto, user);
    }


    @Transactional
    public UserDto updateCurrentUser(UserDto userDto, String currentUserName) {

        User user = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new DataNotFoundException("User with username: "+currentUserName+" does not exist"));
        return updateUser(userDto, user);
    }


    private UserDto updateUser(UserDto userDto, User user) {



        CustomBeanUtils.copyNotNullProperties(userDto, user,"id");

        if(userDto.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        User updatedUsr = userRepository.save(user);
        UserDto updatedUserDto = new UserDto();
        BeanUtils.copyProperties(updatedUsr, updatedUserDto);


        return updatedUserDto;
    }

    @Transactional
    public UserDto updateUserRoles(RolesDto rolesDto) {


        return null;
    }


    @Transactional
    public UserDto getCurrentUserInfo(UserDetails principal) {

        UserDto resultUser = new UserDto();

        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new DataNotFoundException("User with username :"+principal.getUsername()+" does not exist"));

        BeanUtils.copyProperties(user, resultUser);
        return resultUser;
    }


    @Transactional
    public UserDto getUserInfoById(Long userId) {


        UserDto resultUser = new UserDto();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id: "+userId+" does not exist"));

        BeanUtils.copyProperties(user, resultUser);
        return resultUser;
    }
}
