package com.restful.web.service.impl;

import com.restful.web.exceptions.UserServiceExceptions;
import com.restful.web.io.entity.UserEntity;
import com.restful.web.io.repositories.UserRepository;
import com.restful.web.service.UserService;
import com.restful.web.shared.dto.AddressDTO;
import com.restful.web.shared.dto.UserDto;
import com.restful.web.ui.model.response.ErrorMessages;
import com.restful.web.ui.model.response.UserRest;
import com.restful.web.util.Utils;
import lombok.extern.java.Log;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto createUser(UserDto user) {

        if (userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists");

        for (int i = 0; i < user.getAddresses().size(); i++) {
            AddressDTO address = user.getAddresses().get(i);
            address.setUserDetails(user);
            address.setAddressId(utils.generateAddressId(30));
            user.getAddresses().set(i, address);
        }
        UserEntity userEntity = new UserEntity();
        //BeanUtils.copyProperties(user, userEntity);
        ModelMapper modelMapper = new ModelMapper();
        userEntity = modelMapper.map(user, UserEntity.class);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        //Make return type object
        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();

        //BeanUtils.copyProperties(storedUserDetails, returnValue);
        returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserDto returnValue = new UserDto();
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();
        returnValue = modelMapper.map(userEntity, UserDto.class);
        //BeanUtils.copyProperties(userEntity, returnValue);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        UserDto returnValue = new UserDto();

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        if (userDto.getFirstName().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userEntity.setFirstName(userDto.getFirstName());

        if (userDto.getLastName().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updateUserDetails = userRepository.save(userEntity);

        BeanUtils.copyProperties(updateUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null) throw new UserServiceExceptions(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> returnValue = new ArrayList<>();

        if (page > 0) page = page - 1;

        Pageable pageableRequest = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }
        return returnValue;
    }

}
