package com.restful.web.io.repositories;

import com.restful.web.io.entity.AddressEntity;
import com.restful.web.io.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
}
