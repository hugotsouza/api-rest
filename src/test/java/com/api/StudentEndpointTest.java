package com.api;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.api.model.Student;
import com.api.repository.StudentRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT) // configures the test web context with a randomly chosen port.
@AutoConfigureMockMvc
public class StudentEndpointTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;
	@MockBean
	private StudentRepository studentRepository;
	@Autowired
	private MockMvc mockMvc;

	@TestConfiguration
	static class Config {
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthorization("toyo", "devdojo");
		}
	}
	
	@Before
	public void setup(){
		Student student = new Student(1l, "Hugo", "hugo@gmail.com");
		BDDMockito.when(studentRepository.findOne(1l)).thenReturn(student);
	}

	@Test
	public void listStudentsWhenUsernameAndPasswordIncorrectShouldreturnStatusCode401() {
		restTemplate = restTemplate.withBasicAuth("1", "1");
		//String return is useful for catching error messages that 
		//would not return if it were student type
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);

		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void getStudentsByIdWhenUsernameAndPasswordIncorrectShouldreturnStatusCode401() {
		restTemplate = restTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);

		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}

	@Test
	public void listStudentsWhenUsernameAndPasswordCorrectShouldreturnStatusCode200() {
		List<Student> students = Arrays.asList(new Student(1l, "Hugo", "hugo@gmail.com"),
				new Student(2l, "João", "joão@gmail.com"));
		BDDMockito.when(studentRepository.findAll()).thenReturn(students);
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);

		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getStudentsByIdWhenUsernameAndPasswordCorrectShouldreturnStatusCode200() {
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/{id}", String.class, 1l);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordCorrectAndStudentDoesNotExistShouldreturnStatusCode404() {
		ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/{id}", String.class, -1);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistShouldReturnStatusCode200(){

		BDDMockito.doNothing().when(studentRepository).delete(1l);
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1l);
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	//example of use of the MockMvc
	@Test
	@WithMockUser(username="xx", password="xx", roles={"USER","ADMIN"})//only roles are important
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception{
		BDDMockito.doNothing().when(studentRepository).delete(1l);

		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}",-1l))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	////example of use of the MockMvc
	@Test
	@WithMockUser(username="xx", password="xx", roles={"USER"})
	public void deleteWhenUserDoesNotHasRoleAdminShouldReturnStatusCode403() throws Exception{
		BDDMockito.doNothing().when(studentRepository).delete(1l);

		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}",1l))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	////example of use of the MockMvc
	@Test
	public void createWhenNameIsNullShouldReturnStatusCode400(){
		Student student = new Student(1l, null, "hugo@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/admin/students/", student, String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
		Assertions.assertThat(response.getBody()).contains("fieldMessage", "The student name field is required");
	}
	
	@Test
	public void createShouldPersistDataAndReturnStatusCode201(){
		Student student = new Student(3l, "Antônio", "antonio@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.postForEntity("/v1/admin/students/", student, String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
		Assertions.assertThat(response.getStatusCodeValue()).isNotNull();
	}
	
	@Test
	public void updateShouldChangeDataAndReturnStatusCode200(){
		Student student = new Student(1l, "Antônio", "antonio@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.exchange("/v1/admin/students/", HttpMethod.PUT, new HttpEntity<>(student), String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Assertions.assertThat(response.getStatusCodeValue()).isNotNull();
	}

}
