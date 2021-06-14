package washit;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AccountRep;
import washit.dao.AccountRepository;
import washit.entity.Account;
import washit.util.JwtUtil;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ModifyAccountStepDef {
	  @LocalServerPort
	  private int port;
	private String username;
	private String email;
	private String fullName;
	private String password;
	private final RestTemplate restTemplate = new RestTemplate();
	private Account myAccount;
	private ResponseEntity<AccountRep> response;
	private HttpClientErrorException.BadRequest responseError;
	@Autowired
	private AccountRepository accountRepository;
	//jwt
	@Autowired
	private JwtUtil jwtUtil;


	private String accountsEndpoint() {
		final String serverUrl = "http://localhost";
		final String accountsEndpoint = "/accounts";
		return serverUrl + ":" + port + accountsEndpoint;
	}

	@Given("I am a user of Wash-It with the following account information:")
	public void iAmAUserOfWashIt(io.cucumber.datatable.DataTable dataTable) {

		 List<Map<String, String>> list = dataTable.asMaps();
		 username = list.get(0).get("username");
		 password = list.get(0).get("password");
		 email = list.get(0).get("email");
		 fullName = list.get(0).get("fullName");
		 
		 myAccount = new Account(username, email, fullName, password, true);
		 myAccount = accountRepository.save(myAccount);
		 assertNotNull(myAccount);

	  }
	  
	  @When("I provide an {string} with valid information {string}")
	  public void iProvideACategoryWithValidInfo(String category, String valid) {
		  if(category.equals("password")) {
			  myAccount.setPassword(valid.trim());
			  password = valid.trim();

		  }
		  if(category.equals("email")) {
			  myAccount.setEmail(valid.trim());
				email = valid.trim();

			}
			if (category.equals("fullName")) {
				myAccount.setFullName(valid.trim());
				fullName = valid.trim();

			}


			String token = jwtUtil.generateToken(myAccount.getUsername());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			this.response = restTemplate.exchange(
				accountsEndpoint() +
				"/{username}", HttpMethod.PUT, new HttpEntity<>(myAccount, headers), AccountRep.class, myAccount.getUsername()
			);
		}

	  @Then("my account's information should be updated")
	  public void myAccountCategoryShouldHaveTheFollowingInfo() {
		  AccountRep account = response.getBody();
		  assertNotNull(account);
		  assertEquals(account.getUsername(), username);
		  assertEquals(account.getFullName(), fullName);
		  assertEquals(account.getPassword(), password);
		  assertEquals(account.getEmail(), email);
		  assertEquals(HttpStatus.OK, response.getStatusCode());
	  }

	  @When("I provide an {string} with invalid information {string}")
	  public void iProvideACategoryWithInvalidInfo(String category, String invalid) {
		  if(category.equals("password")) {
			  myAccount.setPassword(invalid.trim());
			}
			if (category.equals("email")) {
				myAccount.setEmail(invalid.trim());
			}
			if (category.equals("fullName")) {
				myAccount.setFullName(invalid.trim());
			}

			System.out.println("***********************************" + category + "**************");
			System.out.println("***********************************" + invalid + "**************");
			String token = jwtUtil.generateToken(myAccount.getUsername());
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setBearerAuth(token);
			responseError = assertThrows(
				HttpClientErrorException.BadRequest.class,
				() -> restTemplate.exchange(
					accountsEndpoint() +
					"/{username}", HttpMethod.PUT, new HttpEntity<>(myAccount, headers), AccountRep.class, myAccount.getUsername()
				)
			);
		}

	  @Then("an error message {string} is issued")
	  public void anErrorMessageIsIssued(String string) {

			assertNotNull(responseError);
			assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
			assertEquals(string, responseError.getResponseBodyAsString());
		}
	  
}


