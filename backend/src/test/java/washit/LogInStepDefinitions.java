package washit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import washit.controller.representation.AccountRep;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.util.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LogInStepDefinitions {

	@Autowired
	private AccountRepository accountRepository;

	@LocalServerPort
	private int port;

	@Autowired
	private JwtUtil jwtUtil;
	
	private Account accountSuccessLogin;
	private AccountDto accountDtoSuccessLogin;
	private AccountDto accountDtoTestLogin;
	private String nonExistentAccount;
	private String incorrectPassword = "incorrect";
	HttpHeaders headers;
	private HttpEntity<AccountRep> request;
	private final RestTemplate restTemplate = new RestTemplate();
	boolean isLogInGherkin = false;

	
	String loginResponse;
	
	private String loginEndpoint() {
		final String serverUrl = "http://localhost";
        final String adsEndpoint = "/login";
        return serverUrl + ":" + port + adsEndpoint; 
	}

	@Given("that I have an account for Wash-It")
	public void that_i_have_an_account_for_wash_it() {
		isLogInGherkin = true;
		accountDtoSuccessLogin = new AccountDto();
	}

	@Given("that my username is {string}")
	public void that_my_username_is(String string) {
		accountDtoSuccessLogin.setUsername(string);
	}

	@Given("that my fullname is {string}")
	public void that_my_fullname_is(String string) {
		accountDtoSuccessLogin.setFullName(string);
	}

	@Given("that my password is {string}")
	public void that_my_password_is(String string) {
		accountDtoSuccessLogin.setPassword(string);
	}

	@Given("the logged-in status of my account with username {string} is false")
	public void the_logged_in_status_of_my_account_with_username_is_false(String string) {
		accountDtoSuccessLogin.setLoggedIn(false);
		accountDtoSuccessLogin.setEmail("testLogIn@gmail.com");
		accountSuccessLogin = new Account(accountDtoSuccessLogin.getUsername(), accountDtoSuccessLogin.getEmail(), accountDtoSuccessLogin.getFullName(), accountDtoSuccessLogin.getPassword(), accountDtoSuccessLogin.isLoggedIn());
		
		accountRepository.save(accountSuccessLogin);
	}

	@Given("that the user account with username {string} does not exist in the system")
	public void that_the_user_account_with_username_does_not_exist_in_the_system(String string) {
		nonExistentAccount = string;
	}

	@When("I request to log in")
	public void i_request_to_log_in(){
		headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        accountDtoTestLogin = new AccountDto();
        accountDtoTestLogin.setFullName("Full Name");
        accountDtoTestLogin.setLoggedIn(false);
        
        
	}

	@When("I supply {string} as username")
	public void i_supply_as_username(String string) {
		accountDtoTestLogin.setUsername(string);
	}

	@When("I supply {string} as password")
	public void i_supply_as_password(String string) throws URISyntaxException {

		accountDtoTestLogin.setPassword(string);

		try{
			HttpEntity<AccountRep> request = new HttpEntity<>(AccountRep.fromAccountDto(accountDtoTestLogin), headers);
			URI loginURI = new URI(loginEndpoint());
			loginResponse = restTemplate.postForObject(loginURI, request, String.class);
		}
		catch(HttpClientErrorException e) {
			loginResponse = e.getMessage();
		}
		

		
	}

	@When("I do not supply {string} as password")
	public void i_do_not_supply_as_password(String string) throws URISyntaxException {
		accountDtoTestLogin.setPassword(incorrectPassword);

		try{
			HttpEntity<AccountRep> request = new HttpEntity<>(AccountRep.fromAccountDto(accountDtoTestLogin), headers);
			URI loginURI = new URI(loginEndpoint());
			loginResponse = restTemplate.postForObject(loginURI, request, String.class);
		}
		catch(HttpClientErrorException e) {
			loginResponse = e.getMessage();
		}
	}

	@Then("the error message {string} is returned")
	public void the_error_message_is_returned(String string) {
		assertTrue(loginResponse.contains(string));
	}

	@Then("the logged-in status of my account with username {string} remains false")
	public void the_logged_in_status_of_my_account_with_username_remains_false(String string) {
		try {
			assertFalse(accountDtoTestLogin.getUsername().equals(jwtUtil.getUsernameFromToken(loginResponse).get()));
		}
		catch(Exception e) {
			assertNotEquals(e.getMessage(), accountDtoTestLogin.getUsername());
		}
	}

	@Then("the authentication token is returned")
	public void the_greeting_message_is_returned() {
		assertNotEquals("NonExistentAccount", loginResponse);
		assertNotEquals("Incorrect password", loginResponse);
		assertNotEquals(null, loginResponse);
	}

	@Then("the logged-in status of my account with username {string} is true")
	public void the_logged_in_status_of_my_account_with_username_is_true(String string) {
		assertTrue(accountDtoTestLogin.getUsername().equals(jwtUtil.getUsernameFromToken(loginResponse).get()));
		
	}
	
	@After
	public void cleanUp(){
		if (isLogInGherkin) {
			accountRepository.delete(accountSuccessLogin);
		}
		
	}
}
