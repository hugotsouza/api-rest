package com.api.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
	
	User findByUserName(String userName);//Will be used in the implementation of UserDetailService

}
