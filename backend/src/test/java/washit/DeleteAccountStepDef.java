package washit;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DeleteAccountStepDef {

    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    private int port;

    @Autowired
    private JwtUtil jwtUtil;

    private String accountsEndpoint() {
        final String serverUrl = "http://localhost";
        final String adsEndpoint = "/accounts";
        return serverUrl + ":" + port + adsEndpoint;
    }


    private ResponseEntity<String> response;
    private final RestTemplate restTemplate = new RestTemplate();


    private String username="AccountToDelete";
    private String fullname="first last";
    private String email="user@washit.com";
    private String password="Password123!";
    private boolean loggedin=true;

    private AccountDto accountDto = new AccountDto();

    private String deleteUrl;

    @Before
    public void cleanstart(){
        if (accountRepository.existsById(username))
            accountRepository.deleteById(username);
    }

    @Given("I am logged in as an existing user of Wash-It")
    public void iAmLoggedInAsAnExistingUserOfWashIt() {
        accountDto.setUsername(username);
        accountDto.setFullName(fullname);
        accountDto.setEmail(email);
        accountDto.setPassword(password);
        accountDto.setLoggedIn(loggedin);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccountDto> request = new HttpEntity<>(accountDto, headers);
        restTemplate.postForEntity(accountsEndpoint(),request,AccountDto.class);
    }

    @When("I request to delete my account")
    public void iRequestToDeleteMyAccount() {
        deleteUrl = accountsEndpoint()+"/"+username;
    }

    @And("I confirm to delete my account")
    public void iConfirmToDeleteMyAccount() {
        deleteUrl = deleteUrl + "/" + true;
        String token = jwtUtil.generateToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> request = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, Void.class, new Object());
        } catch (RestClientException ignored) {
            // errors will be checked in the next step's assertions
        }
    }

    @Then("my account is removed from the system")
    public void myAccountIsRemovedFromTheSystem() {
        boolean stillExist = accountRepository.existsByUsername(username);
        assertFalse(stillExist);
    }

    @And("I cancel the confirmation to delete my account")
    public void iCancelTheConfirmationToDeleteMyAccount() {
        deleteUrl = deleteUrl + "/" + false;
        String token = jwtUtil.generateToken(username);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Object> request = new HttpEntity<>(headers);
        try {
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, request, Void.class, new Object());
        } catch (RestClientException ignored) {
            // errors will be checked in the next step's assertions
        }
    }

    @Then("my account is not deleted")
    public void myAccountIsNotDeleted() {
        boolean stillExist = accountRepository.existsByUsername(username);
        assertTrue(stillExist);
    }

    @After
    public void cleanUp(){
        if (accountRepository.existsByUsername(username)) {
            accountRepository.deleteById(username);
        }
    }

}
