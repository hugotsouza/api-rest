package com.api.endpoint;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.error.ResourceNotFoundException;
import com.api.model.Student;
import com.api.repository.StudentRepository;

@RestController
@RequestMapping("v1")//direct versioning at URL
public class StudentEndPoint {

	private final StudentRepository studentDao;

	@Autowired
	public StudentEndPoint(StudentRepository studentDao) {
		this.studentDao = studentDao;
	}
	
	@GetMapping(path="protected/students")//Writing the role in the url simplifies the security setup
	public ResponseEntity<?> listAll(Pageable pageable){
		return new ResponseEntity<>(studentDao.findAll(pageable), HttpStatus.OK);
	}
	
	@GetMapping(path="protected/students/{id}")
	public ResponseEntity<?> getStudentById(@PathVariable("id") long id){
		verifyIfStudentExists(id); 
		
		Student student = studentDao.findOne(id);
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
	
	@GetMapping(path="protected/students/findByName/{name}")
	public ResponseEntity<?> findStudentByName(@PathVariable String name){
			return new ResponseEntity<>(studentDao.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
	}
	
	@PostMapping(path="admin/students")
	@Transactional(rollbackFor=Exception.class)
	public ResponseEntity<?> save(@Valid @RequestBody Student student){
		return new ResponseEntity<>(studentDao.save(student), HttpStatus.CREATED);
	}
	
	@DeleteMapping(path="admin/students/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id){
		verifyIfStudentExists(id);
		
		studentDao.delete(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping(path="admin/students")
	public ResponseEntity<?> update(@RequestBody Student student){
		verifyIfStudentExists(student.getId());
		
		studentDao.save(student);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/***
	 * Helper method that will check 
	 * if a resource exists in the database
	 * @param id - resource id 
	 */
	private void verifyIfStudentExists(long id){
		if(studentDao.findOne(id) == null)
			throw new ResourceNotFoundException("Student not found for ID: " + id);
	}
	
}
	