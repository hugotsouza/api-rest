package com.api;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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
public class StudentEndpointTokenTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@LocalServerPort
	private int port;
	@MockBean
	private StudentRepository studentRepository;
	@Autowired
	private MockMvc mockMvc;
	private HttpEntity<Void> protectedHeader;
	private HttpEntity<Void> adminHeader; 
	private HttpEntity<Void> wrongHeader;
	
	@Before
	public void configProtectedHeaders() {
		String str = "{\"username\" : \"oda\",\"password\" : \"root\"}";
		HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
		this.protectedHeader = new HttpEntity<>(headers);
	}
	@Before
	public void configAdminHeaders() {
		String str = "{\"username\" : \"root\",\"password\" : \"root\"}";
		HttpHeaders headers = restTemplate.postForEntity("/login", str, String.class).getHeaders();
		this.adminHeader = new HttpEntity<>(headers);
	}
	
	@Before
	public void configWrongHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "3924230hnd3");
		this.wrongHeader = new HttpEntity<>(headers);
	}
	
	@Before
	public void setup(){
		Student student = new Student(1l, "Hugo", "hugo@gmail.com");
		BDDMockito.when(studentRepository.findOne(1l)).thenReturn(student);
	}

	@Test
	public void listStudentsWhenTokenIsIncorrectShouldreturnStatusCode403() {
		//String return is useful for catching error messages that 
		//would not return if it were student type
		ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", HttpMethod.GET, wrongHeader, String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
	}

	@Test
	public void getStudentsByIdWhenTokenIsIncorrectShouldreturnStatusCode403() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", HttpMethod.GET, wrongHeader, String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
	}

	@Test
	public void listStudentsWhenTokenIsCorrectShouldreturnStatusCode200() {
		ResponseEntity<String> response = restTemplate.exchange("/v1/protected/students/", HttpMethod.GET, protectedHeader, String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}

	@Test
	public void getStudentsByIdWhenTokenIsCorrectShouldreturnStatusCode200() {
		ResponseEntity<Student> response = restTemplate.exchange("/v1/protected/students/1",  HttpMethod.GET, protectedHeader, Student.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdWhenTokenIsCorrectAndStudentDoesNotExistShouldreturnStatusCode404() {
		ResponseEntity<Student> response = restTemplate.exchange("/v1/protected/students/-1",  HttpMethod.GET, protectedHeader, Student.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistShouldReturnStatusCode200(){
		BDDMockito.doNothing().when(studentRepository).delete(1l);
		
		ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/1", HttpMethod.DELETE, adminHeader, String.class);
		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	//example of use of the MockMvc
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception{
		String token = adminHeader.getHeaders().get("Authorization").get(0);
		BDDMockito.doNothing().when(studentRepository).delete(1l);
		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}",-1l).header("Authorization", token))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	////example of use of the MockMvc
	@Test
	public void deleteWhenUserDoesNotHasRoleAdminShouldReturnStatusCode403() throws Exception{
		String token = protectedHeader.getHeaders().get("Authorization").get(0);
		BDDMockito.doNothing().when(studentRepository).delete(1l);

		mockMvc.perform(MockMvcRequestBuilders.delete("/v1/admin/students/{id}",1l).header("Authorization", token))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}
	////example of use of the MockMvc
	@Test
	public void createWhenNameIsNullShouldReturnStatusCode400(){
		Student student = new Student(1l, null, "hugo@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.exchange("/v1/admin/students/",HttpMethod.POST, new HttpEntity<>(student, adminHeader.getHeaders()), String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
		Assertions.assertThat(response.getBody()).contains("fieldMessage", "The student name field is required");
	}
	
	@Test
	public void createShouldPersistDataAndReturnStatusCode201(){
		Student student = new Student(3l, "Antônio", "antonio@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.exchange("/v1/admin/students/",HttpMethod.POST, new HttpEntity<>(student, adminHeader.getHeaders()), String.class);
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
		Assertions.assertThat(response.getStatusCodeValue()).isNotNull();
	}
	
	@Test
	public void updateShouldChangeDataAndReturnStatusCode200(){
		Student student = new Student(1l, "Antônio", "antonio@gmail.com");
		BDDMockito.when(studentRepository.save(student)).thenReturn(student);
		ResponseEntity<String> response = restTemplate.exchange("/v1/admin/students/", HttpMethod.PUT, new HttpEntity<>(student, adminHeader.getHeaders()), String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
		Assertions.assertThat(response.getStatusCodeValue()).isNotNull();
	}

}
