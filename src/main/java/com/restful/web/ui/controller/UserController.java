package com.restful.web.ui.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.restful.web.service.UserService;
import com.restful.web.shared.dto.UserDto;
import com.restful.web.ui.model.request.UserDetailsRequestModel;
import com.restful.web.ui.model.response.UserRest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;
    @GetMapping
    public UserRest getUser(){

        return null;
    }

    @PostMapping
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails){
        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping
    public String updateUser(){
        return "Update User!!!!";
    }

    @DeleteMapping
    public String deleteUser(){
        return "Delete User!!!!";
    }
}
