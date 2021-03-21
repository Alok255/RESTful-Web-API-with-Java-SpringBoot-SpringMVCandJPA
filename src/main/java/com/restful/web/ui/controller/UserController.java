package com.restful.web.ui.controller;

import com.restful.web.exceptions.UserServiceExceptions;
import com.restful.web.service.AddressesService;
import com.restful.web.service.UserService;
import com.restful.web.shared.dto.AddressDTO;
import com.restful.web.shared.dto.UserDto;
import com.restful.web.ui.model.request.UserDetailsRequestModel;
import com.restful.web.ui.model.response.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    AddressesService addressesService;

    @GetMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest userRestModel = new UserRest();

        UserDto userDto = userService.getUserByUserId(id);
        ModelMapper modelMapper = new ModelMapper();

        Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withSelfRel();
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddresses(id)).withRel("addresses");

        userRestModel = modelMapper.map(userDto, UserRest.class);
        userRestModel.add(userLink);
        userRestModel.add(addressLink);
        //BeanUtils.copyProperties(userDto, returnValue);
        return userRestModel;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {

        if (userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty() || userDetails.getEmail().isEmpty() || userDetails.getPassword().isEmpty())
            throw new UserServiceExceptions(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserRest returnValue = new UserRest();

        /*UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);*/
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        returnValue = modelMapper.map(createdUser, UserRest.class);
        //BeanUtils.copyProperties(createdUser, returnValue);

        return returnValue;
    }

    @PutMapping(path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

        UserRest returnValue = new UserRest();

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updateUser, returnValue);

        return returnValue;
    }

    @DeleteMapping(path = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public OperationStatusModel deleteUser(@PathVariable String id) {

        OperationStatusModel returnValue = new OperationStatusModel();
        returnValue.setOperationName(RequestOperationName.DELETE.name());

        userService.deleteUser(id);

        returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
        return returnValue;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto : users) {
            //UserRest userModel = new UserRest();
           // BeanUtils.copyProperties(userDto, userModel);
            ModelMapper modelMapper = new ModelMapper();
            Link userLink = linkTo(methodOn(UserController.class).getUser(userDto.getUserId())).withSelfRel();
            Link addressLink = linkTo(methodOn(UserController.class).getUserAddresses(userDto.getUserId())).withRel("addresses");

            UserRest userModel = modelMapper.map(userDto, UserRest.class);

            userModel.add(userLink);
            userModel.add(addressLink);

            returnValue.add(userModel);
        }

        return returnValue;
    }

    //http://localhost:8080/restfulweb/users/{userId}/addresses
    @GetMapping(path = "/{id}/addresses",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<AddressesRest> getUserAddresses(@PathVariable String id) {

        List<AddressesRest> addressesListRestModel = new ArrayList<>();

        List<AddressDTO> addressesDTO = addressesService.getAddresses(id);

        if (addressesDTO != null && !addressesDTO.isEmpty()) {
            Type listType = new TypeToken<List<AddressesRest>>() {
            }.getType();
            addressesListRestModel = new ModelMapper().map(addressesDTO, listType);

            for(AddressesRest addressRest : addressesListRestModel){
                Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(id, addressRest.getAddressId())).withSelfRel();
                addressRest.add(addressLink);

                Link userLink = linkTo(methodOn(UserController.class).getUser(id)).withRel("user");
                addressRest.add(userLink);
            }
        }

        return addressesListRestModel;
    }

    //http://localhost:8080/restfulweb/users/{userId}/addresses/{addressId}
    @GetMapping(path = "/{userId}/addresses/{addressId}",
    produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public AddressesRest getUserAddress(@PathVariable String userId, @PathVariable String addressId){

        AddressDTO returnValue = addressesService.getAddress(addressId);

        ModelMapper modelMapper = new ModelMapper();
        Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
        Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
        Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(addressId)).withRel("addresses");

       AddressesRest addressesRestModel = modelMapper.map(returnValue, AddressesRest.class);
       addressesRestModel.add(addressLink);
       addressesRestModel.add(userLink);
       addressesRestModel.add(addressesLink);

       return addressesRestModel;

    }
}
