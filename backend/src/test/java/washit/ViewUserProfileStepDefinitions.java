package washit;

import io.cucumber.java.en.And;
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
import washit.dto.AccountDto;



import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ViewUserProfileStepDefinitions {


    private HttpStatus responseStatus;
    private String responseMessage;

    @Autowired
    private AccountRepository accountRepository;

    @LocalServerPort
    private int port;

    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    private String accountsEndpoint() {
        final String serverUrl = "http://localhost";
        final String adsEndpoint = "/accounts";
        return serverUrl + ":" + port + adsEndpoint;
    }

    private String loginEndpoint() {
        final String serverUrl = "http://localhost";
        final String adsEndpoint = "/login";
        return serverUrl + ":" + port + adsEndpoint;
    }

    private AccountDto accountDto = new AccountDto();

    private String username="Viewer";
    private String fullname="Apple Banana";
    private String email="user@washit.com";
    private String password="Password1!";

    private AccountDto accountDto_profile = new AccountDto();
    private AccountRep accountRep = new AccountRep();

    private String username_profile="Profile";
    private String fullname_profile="Orange Mango";
    private String email_profile="user_profile@washit.com";
    private String password_profile="Password2!";

    private ResponseEntity<AccountDto> responseProfile;

    @Given("I am logged into Wash-It")
    public void loggedIntoWashIt() throws URISyntaxException {


        // create account
        accountDto.setUsername(username);
        accountDto.setFullName(fullname);
        accountDto.setEmail(email);
        accountDto.setPassword(password);
        accountDto.setLoggedIn(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccountDto> request = new HttpEntity<>(accountDto, headers);
        restTemplate.postForEntity(accountsEndpoint(),request,AccountDto.class);

        //log into account
        HttpEntity<AccountRep> request2 = new HttpEntity<>(AccountRep.fromAccountDto(accountDto), headers);
        URI loginURI = new URI(loginEndpoint());
        restTemplate.postForObject(loginURI, request2, String.class);
    }


    // ------------------------------- NORMAL FLOW ----------------------------------

    @And("There exists another user of Wash-It")
    public void anotherUserExists() {

        accountDto_profile.setUsername(username_profile);
        accountDto_profile.setFullName(fullname_profile);
        accountDto_profile.setEmail(email_profile);
        accountDto_profile.setPassword(password_profile);
        accountDto_profile.setLoggedIn(false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AccountDto> request = new HttpEntity<>(accountDto_profile, headers);
        restTemplate.postForEntity(accountsEndpoint(),request,AccountDto.class);

    }

    @When("I request to view a users profile")
    public void requestProfile() throws URISyntaxException {
        url = accountsEndpoint()+"/"+username_profile;
        URI getProfile = new URI(url);
        this.responseProfile = restTemplate.getForEntity(getProfile, AccountDto.class);
    }

    @Then("The users profile information should be returned")
    public void returnedProfile() {
        AccountDto accountDto_returned_profile = responseProfile.getBody();
        assertNotNull(accountDto_returned_profile);
        assertEquals(accountDto_profile, accountDto_returned_profile);

        accountRepository.delete(accountRepository.findByUsername(accountDto_profile.getUsername()));
        accountRepository.delete(accountRepository.findByUsername(accountDto.getUsername()));

    }

    // ------------------------------- ERROR FLOW ----------------------------------

    @When("I request to view the profile of a user that deleted his or her account")
    public void requestProfileForUserThatDoesNotExist() throws URISyntaxException {

        String NON_EXISTING_USERNAME = "random";
        url = accountsEndpoint()+"/"+NON_EXISTING_USERNAME;
        URI getProfile = new URI(url);
        try {
            this.responseProfile = restTemplate.getForEntity(getProfile, AccountDto.class);
        }
        catch (HttpClientErrorException.BadRequest e) {
            responseMessage = e.getMessage();
        }
    }

    @Then("The system should return an error")
    public void profileDoesNotExist() {
//        AccountDto accountDto_returned_profile = responseProfile.getBody();
//        assertNotNull(accountDto_returned_profile);

//        assertEquals(HttpStatus.BAD_REQUEST, responseStatus);
        assertEquals("400 : [No Account with username [random] exists]",responseMessage);

        accountRepository.delete(accountRepository.findByUsername(accountDto.getUsername()));


    }


}
