package com.restful.web.service.impl;

import com.restful.web.UserRepository;
import com.restful.web.io.entity.UserEntity;
import com.restful.web.service.UserService;
import com.restful.web.shared.dto.UserDto;
import com.restful.web.util.Utils;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Logger
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto getUser(){
        /*UserDto returnValue = new UserDto();

        public List<UserDto> findAll() {
            List<UserDto> students = new ArrayList<>();
            userRepository.findAll().forEach(students::add);
            return students;
        }
        BeanUtils.copyProperties(userRepository.findAll(), returnValue);
         */
        return null;
    }

    @Override
    public UserDto createUser(UserDto user) {

       if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);
        String publicUserId  = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //Make return type object
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();

        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
