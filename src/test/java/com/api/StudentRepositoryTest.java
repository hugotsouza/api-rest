package com.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.model.Student;
import com.api.repository.StudentRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) configures the use of the application database
public class StudentRepositoryTest {

	@Autowired
	private StudentRepository repository;
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void createShouldPersistData(){
		Student student = new Student("Hugo", "souzahugo@gmail.com");
		this.repository.save(student);
		
		Assertions.assertThat(student.getId()).isNotNull();
		Assertions.assertThat(student.getName()).isEqualTo("Hugo");
		Assertions.assertThat(student.getEmail()).isEqualTo("souzahugo@gmail.com");
	}
	
	@Test
	public void deleteShouldRemoveData(){
		Student student = new Student("Hugo", "souzahugo@gmail.com");
		this.repository.save(student);
		this.repository.delete(student.getId());
		
		assertThat(repository.findOne(student.getId())).isNull();
	}
	
	@Test
	public void updateShouldChangeAndPersistData(){
		Student student = new Student("Hugo", "souzahugo@gmail.com");
		student = this.repository.save(student);
		
		assertThat(repository.findOne(student.getId())).isNotNull();
		
		student.setEmail("hugosouza@gmail.com");
		student.setName("Trindade");
		student = this.repository.save(student);
		
		assertThat(repository.findOne(student.getId()).getEmail()).isEqualTo("hugosouza@gmail.com");
		assertThat(repository.findOne(student.getId()).getName()).isEqualTo("Trindade");
	}
	
	@Test
	public void updateShouldChangePersistData(){
		Student student = new Student("Hugo", "souzahugo@gmail.com");
		Student student2 = new Student("hugo", "souza@gmail.com");
		
		student = this.repository.save(student);
		student2 = this.repository.save(student2);
		List<Student> studentList = repository.findByNameIgnoreCaseContaining("Hugo");
		assertThat(studentList.size()).isEqualTo(2);
	}
	
	@Test
	public void createWhenNameIsNullShouldThrowConstraintViolationException(){
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("The student name field is required");
		this.repository.save(new Student());
	}
	
	@Test
	public void createWhenEmailIsNullShouldThrowConstraintViolationException(){
		thrown.expect(ConstraintViolationException.class);
		Student student = new Student();
		student.setName("Hugo");
		this.repository.save(student);
	}
	
	@Test
	public void createWhenEmailIsNotValidShouldThrowConstraintViolationException(){
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("Enter a valid email address");
		Student student = new Student();
		student.setName("Hugo");
		student.setEmail("hugogmail.com");
		this.repository.save(student);
	}
}
