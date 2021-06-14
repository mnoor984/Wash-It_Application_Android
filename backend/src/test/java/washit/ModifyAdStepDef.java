package washit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AdRep;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.entity.Account;
import washit.entity.Ad;
import washit.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ModifyAdStepDef {
  @LocalServerPort
  private int port;
  private final RestTemplate restTemplate = new RestTemplate();
  private AdRep myPostedAd;
  private ResponseEntity<AdRep> response;
  private String newPhoneNum;
  private HttpClientErrorException.BadRequest responseError;
  private String newClothingDesc;
  private Account testAccount;
  private Ad testAd;
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private AdRepository adRepository;

  private String adsEndpoint() {
    final String serverUrl = "http://localhost";
    final String adsEndpoint = "/ads";
    return serverUrl + ":" + port + adsEndpoint;
  }

  @Given("I am a Customer of WashIt")
  public void iAmACustomerOfWashIt() {
    testAccount = new Account(
      "ModifyAdTestAccount", "modifyAdTestAccount@gmail.com", "modify ad", "password", true
    );
    testAccount = accountRepository.save(testAccount);
  }


  @And("I have posted an ad")
  public void iHavePostedAnAd() {

    myPostedAd = new AdRep();
    myPostedAd.setAddress("101 some street");
    myPostedAd.setZipcode("ABC123");
    myPostedAd.setPickupStart("2021-02-10T10:00");
    myPostedAd.setPickupEnd("2021-02-10T11:00");
    myPostedAd.setDropoffStart("2021-02-10T15:00");
    myPostedAd.setDropoffEnd("2021-02-10T16:00");
    myPostedAd.setClothingDesc("20 shirts");
    myPostedAd.setWeight(2.3f);
    myPostedAd.setSpecialInst("No special instructions");
    myPostedAd.setBleach(false);
    myPostedAd.setIron(true);
    myPostedAd.setFold(true);
    myPostedAd.setPhoneNum("111-111-1111");
    myPostedAd.setAccount(testAccount.getUsername());
    testAd = adRepository.save(Ad.from(AdRep.toAdDto(myPostedAd)));
    myPostedAd.setId(String.valueOf(testAd.getId()));
  }

  private void afterEach() {
    adRepository.delete(testAd);
    accountRepository.delete(testAccount);
  }

  @When("I enter a new phone number")
  public void iEnterANewPhoneNumber() {
    newPhoneNum = "222-222-2222";
    myPostedAd.setPhoneNum(newPhoneNum);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String token = jwtUtil.generateToken("ModifyAdTestAccount");
    headers.setBearerAuth(token);
    this.response = restTemplate.exchange(
      adsEndpoint() + "/{id}", HttpMethod.PUT, new HttpEntity<>(myPostedAd, headers), AdRep.class, myPostedAd.getId()
    );
  }

  @Then("The system will update the phone number on the specific ad")
  public void theSystemWillUpdateThePhoneNumberOnTheSpecificAd() {
    final AdRep ad = response.getBody();
    assertNotNull(ad);
    assertEquals(newPhoneNum, ad.getPhoneNum());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    afterEach();
  }

  @When("I enter a drop-off window that comes before the pick-up window")
  public void iEnterADropOffWindowThatComesBeforeThePickUpWindow() {
    myPostedAd.setPickupStart("2021-02-10T10:00");
    myPostedAd.setPickupEnd("2021-02-10T11:00");
    myPostedAd.setDropoffStart("2021-02-10T09:00");
    myPostedAd.setDropoffEnd("2021-02-10T10:00");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    responseError = assertThrows(
      HttpClientErrorException.BadRequest.class,
      () -> restTemplate.exchange(
        adsEndpoint() + "/{id}", HttpMethod.PUT, new HttpEntity<>(myPostedAd, headers), AdRep.class, myPostedAd.getId()
      )
    );
  }

  @Then("I should see an error message")
  public void iShouldSeeAnErrorMessage() {
    assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
    afterEach();
  }

  @When("I enter the new clothing description")
  public void iEnterTheNewClothingDescription() {
    newClothingDesc = "Clothing description 2";
    myPostedAd.setClothingDesc(newClothingDesc);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    String token = jwtUtil.generateToken("ModifyAdTestAccount");
    headers.setBearerAuth(token);
    this.response = restTemplate.exchange(
      adsEndpoint() + "/{id}", HttpMethod.PUT, new HttpEntity<>(myPostedAd, headers), AdRep.class, myPostedAd.getId()
    );
  }

  @Then("The system will update the clothing description on the ad")
  public void theSystemWillUpdateTheClothingDescriptionOnTheAd() {
    final AdRep ad = response.getBody();
    assertNotNull(ad);
    assertEquals(newClothingDesc, ad.getClothingDesc());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    afterEach();
  }

}
