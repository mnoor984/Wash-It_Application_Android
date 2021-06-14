package washit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AdRep;
import washit.dao.AdRepository;
import washit.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateAdStepDef {
    @LocalServerPort
    private int port;
    private final RestTemplate restTemplate = new RestTemplate();
    private String customer;
    private AdRep ad;
    private String phoneNum;
    private String clothingDesc;
    private Float weight;
    private String pickupStart;
    private String pickupEnd;
    private String dropoffStart;
    private String dropoffEnd;
    private String address;
    private String zipcode;
    private Boolean bleach;
    private Boolean iron;
    private Boolean fold;
    private String specialInstructions;
    private ResponseEntity<AdRep> response;
    private HttpClientErrorException.BadRequest responseError;
    //jwt
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdRepository adRepository;

    private String adsEndpoint() {
        final String serverUrl = "http://localhost";
        final String adsEndpoint = "/ads";
        return serverUrl + ":" + port + adsEndpoint;
    }

    private AdRep createAd() {

        AdRep ad = new AdRep();
        this.customer = "testuserDoNotDelete";
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

        return ad;
    }

    @When("I enter a phone number")
    public void iEnterAPhoneNumber() {
        this.phoneNum = "012-345-6789";
    }

    @And("I enter a clothing description")
    public void iEnterAClothingDescription() {
        this.clothingDesc = "4 pairs of pants, 10 pairs of underwear, 10 t-shirts, 12 pairs of socks";
    }

    @And("I enter a weight")
    public void iEnterAWeight() {
        this.weight = Float.valueOf(10.0f);
    }

    @And("I enter a pickup window")
    public void iEnterAPickupWindow() {
        this.pickupStart = "2021-02-09T10:00";
        this.pickupEnd = "2021-02-09T11:00";
    }

    @And("I enter a dropoff window")
    public void iEnterADropoffWindow() {
        this.dropoffStart = "2021-02-09T16:00";
        this.dropoffEnd = "2021-02-09T17:00";
    }

    @And("I enter an address")
    public void iEnterAnAddress() {
        this.address = "123 Wash-it Street";
    }

    @And("I enter a zip code")
    public void iEnterAZipcode() {
        this.zipcode = "H2X2B8";
    }

    @And("I choose services")
    public void iChooseServices() {
        this.bleach = Boolean.TRUE;
        this.iron = Boolean.FALSE;
        this.fold = Boolean.TRUE;
    }

    @And("I enter special instructions")
    public void iEnterSpecialInstructions() {
        this.specialInstructions = "Blue t-shirt does not go in the dryer";
    }

    @And("the pickup window date is after the dropoff window date")
    public void thePickupWindowDateIsBeforeTheDropoffWindowDate() {
        this.pickupStart = "2021-02-10T10:00";
        this.pickupEnd = "2021-02-10T11:00";
    }

    @And("the pickup window is too short")
    public void thePickupWindowIsTooShort() {
        this.pickupStart = "2021-02-09T10:00";
        this.pickupEnd = "2021-02-09T10:30";
    }

    @And("the dropoff window is too short")
    public void theDropoffWindowStartIsTooShort() {
        this.dropoffStart = "2021-02-09T16:00";
        this.dropoffEnd = "2021-02-09T16:30";
    }

    @And("the dropoff window is too close to the pickup window")
    public void theDropoffWindowIsTooCloseToThePickUpWindow() {
        this.dropoffStart = "2021-02-09T11:00";
        this.dropoffEnd = "2021-02-09T12:00";
    }

    @Then("The system will create an ad with this information")
    public void theSystemWillCreateAnAdWithThisInformation() {

        this.ad = createAd();

        // jwt
        String token = jwtUtil.generateToken("testuserDoNotDelete");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

        this.response = restTemplate.postForEntity(adsEndpoint(), request, AdRep.class);

        final AdRep adResp = response.getBody();

        assertNotNull(adResp);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String id = adResp.getId();
        adRepository.deleteById(Integer.parseInt(id));
    }

    @Then("an error message is displayed")
    public void anErrorMessageIsDisplayed() {

        this.ad = createAd();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

        responseError = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForEntity(
                        adsEndpoint(), request, AdRep.class
                )
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
    }
}
