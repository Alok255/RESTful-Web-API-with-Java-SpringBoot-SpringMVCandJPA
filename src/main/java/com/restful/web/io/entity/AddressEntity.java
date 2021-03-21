package com.restful.web.io.entity;

import com.restful.web.shared.dto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "addresses")
public class AddressEntity implements Serializable {

    private static final long serialVersionUID = 1l;

    @Id
    @GeneratedValue
    private long Id;

    @Column(length = 30, nullable = false)
    private String addressId;

    @Column(length = 100, nullable = false)
    private String streetName;

    @Column(length = 30, nullable = false)
    private String city;

    @Column(length = 10, nullable = false)
    private String pinCode;

    @Column(length = 30, nullable = false)
    private String state;

    @Column(length = 30, nullable = false)
    private String country;

    @Column(length = 30, nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }


}