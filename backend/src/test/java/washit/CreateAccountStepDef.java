package washit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.entity.Account;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateAccountStepDef {
	@LocalServerPort
    private int port;
    private final RestTemplate restTemplate = new RestTemplate();
    private AccountDto acc;
    private Account test;
    private ResponseEntity<AccountDto> response;
    private HttpClientErrorException.BadRequest responseError;
    
    @Autowired
    private AccountRepository accountRepository;
    
    private String fullName;
    private String email;
    private String username;
    private String password;
 
    private String accountsEndpoint() {
        final String serverUrl = "http://localhost";
        final String adsEndpoint = "/accounts";
        return serverUrl + ":" + port + adsEndpoint;
    }
    
    private AccountDto createDto() {
    	AccountDto dto = new AccountDto();
    	dto.setEmail(email);
    	dto.setFullName(fullName);
    	dto.setPassword(password);
    	dto.setUsername(username);
    	return dto;
    }
    
    @Given("I do not already have an account for Wash-It")
    public void iDoNotAlreadyHaveAnAccountForWashIt() {}
    
    @Given("an account with the username {string} already exists")
    public void anAccountWithTheUsernameAlreadyExists(String aString) {
    	test = new Account(aString, "testCreateAccount@gmail.com", "Create Account", "TestUser101!", false);
    	test = accountRepository.save(test);
        assertNotNull(test);
    }
  
    @When("I fill in the full name field with {string}")
    public void iFillInTheFullNameFieldWith(String aString) {
    	fullName = aString;
    }
    
    @And("I fill in the email field with {string}")
    public void iFillInTheEmailFieldWith(String aString) {
    	email = aString;
    }
    
    @And("I fill in the username field with {string}")
    public void iFillInTheUsernameFieldWith(String aString) {
    	username = aString;
    }
    
    @And("I fill in the password field with {string}")
    public void iFillInThePasswordFieldWith(String aString) {
    	password = aString; 
    }
    
    @Then("my account should be created with the provided information")
    public void myAccountShouldBeCreatedWithTheProvidedInformation() {
  
    	acc = createDto();
    	HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountDto> request = new HttpEntity<>(acc, headers);
       
        response = restTemplate.postForEntity(accountsEndpoint(), request, AccountDto.class);
        
    	final AccountDto acc = response.getBody();
    	assertNotNull(acc);
    	assertNull(responseError);
    	assertEquals(HttpStatus.CREATED, response.getStatusCode());
    	assertEquals(email, acc.getEmail());
    	assertEquals(password, acc.getPassword());
    	assertEquals(username, acc.getUsername());
    	assertEquals(fullName, acc.getFullName());
    	
    	accountRepository.delete(accountRepository.findByUsername(acc.getUsername()));
    }
    
    @Then("an error message {string} is displayed")
    public void anErrorMessageIsDisplayed(String aString) {
    	
    	acc = createDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AccountDto> request = new HttpEntity<>(acc, headers);

        responseError = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForEntity(
                        accountsEndpoint(), request, AccountDto.class
                )
        );
        
    	assertNotNull(responseError);
    	assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
    	assertEquals(responseError.getResponseBodyAsString(), aString);
    	if(test != null) {
    		accountRepository.delete(test);
    		test = null;
    	}
    	
    }
    
}
