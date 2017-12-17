# Spring boot + Spring Data + Spring Security 

	O projeto trata-se de uma API REST que faz o uso da paginação através da Interface 'Paginable' 
do Spring Data, manipulação de exceções em classes configuradas como @ControllerAdvice, encapsulando 
erros em estruturas padrões ao cliente da API, através de classes do tipo ResponseEntityExceptionHandler.
Por fim, testes dos endpoints usando TestRestTemplate, MockMvc e Mockito assim como dos repositorios
com Assertions. Todas  as classes utilizam as configurações do Spring Test através da anotação 
@SpringBootTest.
