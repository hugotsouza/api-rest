package com.api.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.model.Student;

public interface StudentRepository extends PagingAndSortingRepository<Student, Long>{
	List<Student> findByNameIgnoreCaseContaining(String name);
}
