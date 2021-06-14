package washit;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AdRep;
import washit.dao.AdRepository;
import washit.entity.Ad;
import washit.entity.LaundryStatus;
import washit.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RemoveAdStepDefinitons {
  @Autowired
  private AdRepository adRepository;

  @LocalServerPort
  private int port;

  private final RestTemplate restTemplate = new RestTemplate();
  private String customer;
  private AdRep ad;
  private String phoneNum = "012-345-6789";
  private String clothingDesc = "2 jeans, 3 t-shirts";
  private Float weight = 5.0f;
  private String pickupStart = "2021-02-09T10:00";
  private String pickupEnd = "2021-02-09T11:00";
  private String dropoffStart = "2021-02-12T10:00";
  private String dropoffEnd = "2021-02-12T11:00";
  private String address = "111 road";
  private String zipcode = "A1B2C3";
  private Boolean bleach = true;
  private Boolean iron = false;
  private Boolean fold = true;
  private String specialInstructions = "fold pants into a swan shape";
  private String adRepId;
  private ResponseEntity<AdRep> responseCreate;
  private ResponseEntity<AdRep> responseGet;
  private HttpClientErrorException.BadRequest responseError;
  @Autowired
  private JwtUtil jwtUtil;

  private String adsEndpoint() {
    final String serverUrl = "http://localhost";
    final String adsEndpoint = "/ads";
    return serverUrl + ":" + port + adsEndpoint;
  }


  @Given("I am a Customer using WashIt")
  public void iAmACustomerUsingWashIt() {
    this.customer = "testuserDoNotDelete";
  }

  @Given("I have posted an ad that does not have an accepted bid")
  public void i_have_posted_an_ad_that_does_not_have_an_accepted_bid() {

    AdRep ad = new AdRep();
    ad.setAccount(customer);
    ad.setPhoneNum(phoneNum);
    ad.setClothingDesc(clothingDesc);
    ad.setWeight(weight);
    ad.setPickupStart(pickupStart);
    ad.setPickupEnd(pickupEnd);
    ad.setDropoffStart(dropoffStart);
    ad.setDropoffEnd(dropoffEnd);
    ad.setAddress(address);
    ad.setZipcode(zipcode);
    ad.setBleach(bleach);
    ad.setIron(iron);
    ad.setFold(fold);
    ad.setSpecialInst(specialInstructions);
    ad.setLaundryStatus(LaundryStatus.POSTED); //no accepted bid

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(jwtUtil.generateToken("testuserDoNotDelete"));
    HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

    this.responseCreate = restTemplate.postForEntity(adsEndpoint(), request, AdRep.class);

    final AdRep adRep = responseCreate.getBody();

    this.adRepId = responseCreate.getBody().getId();


    assertNotNull(adRep);
    assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

  }

  @Given("I have posted an ad that does have an accepted bid")
  public void i_have_posted_an_ad_that_does__have_an_accepted_bid() {

    AdRep ad = new AdRep();
    ad.setAccount(customer);
    ad.setPhoneNum(phoneNum);
    ad.setClothingDesc(clothingDesc);
    ad.setWeight(weight);
    ad.setPickupStart(pickupStart);
    ad.setPickupEnd(pickupEnd);
    ad.setDropoffStart(dropoffStart);
    ad.setDropoffEnd(dropoffEnd);
    ad.setAddress(address);
    ad.setZipcode(zipcode);
    ad.setBleach(bleach);
    ad.setIron(iron);
    ad.setFold(fold);
    ad.setSpecialInst(specialInstructions);

    //jwt
    String token = jwtUtil.generateToken("testuserDoNotDelete");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

    this.responseCreate = restTemplate.postForEntity(adsEndpoint(), request, AdRep.class);

    final AdRep adRep = responseCreate.getBody();

    this.adRepId = responseCreate.getBody().getId();

    AdRep inProgressAd = responseCreate.getBody();
    inProgressAd.setLaundryStatus(LaundryStatus.INPROGRESS);

    assertNotNull(adRep);
    assertEquals(HttpStatus.OK, responseCreate.getStatusCode());

    restTemplate.exchange(
      adsEndpoint() + "/{id}", HttpMethod.PUT, new HttpEntity<>(inProgressAd, headers), AdRep.class, this.adRepId
    );

  }


  @When("I try to remove the ad")
  public void i_try_to_remove_the_ad() {

    //jwt
    String token = jwtUtil.generateToken("testuserDoNotDelete");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<AdRep> request = new HttpEntity<>(headers);

    try {
      restTemplate.exchange(adsEndpoint() + "/{id}", HttpMethod.DELETE, request, String.class, this.adRepId);
    } catch (RestClientException ignored) {
      // errors will be checked in the next step's assertions
    }
  }

  @Then("the ad shall not exist on the system")
  public void the_ad_shall_not_exist_on_the_system() {
    assertFalse(adRepository.existsById(Integer.parseInt(this.adRepId)));
  }


  @Then("the ad shall exist on the system")
  public void the_ad_shall_exist_on_the_system() {

    AdRep adRep = null;
    responseGet = restTemplate.getForEntity(adsEndpoint() + "/{id}", AdRep.class, this.adRepId);
    adRep = responseCreate.getBody();


    assertNotNull(adRep);

  }

  @Then("the system shall notify an error message {string}")
  public void the_system_shall_notify_an_error_message(String string) {

    responseError = assertThrows(
      HttpClientErrorException.BadRequest.class,
      () -> restTemplate.delete(
        adsEndpoint() + "/{id}", this.adRepId
      )
    );

    assertEquals(HttpStatus.BAD_REQUEST, this.responseError.getStatusCode());

    // tear down
    Ad adToDelete = new Ad();
    adToDelete.setId(Integer.parseInt(this.adRepId));
    adRepository.delete(adToDelete);
  }


}