package washit;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AdRep;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.AccountDto;
import washit.dto.AdDto;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.entity.LaundryStatus;
import washit.util.JwtUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CreateBidStepDefinitions {

    @LocalServerPort
    private int port;
    private final RestTemplate restTemplate = new RestTemplate();
    private Account adCreatorEntity;
    private Account bidCreatorEntity;
    private AdDto adDto;
    private Ad adEntity;
    private Integer adId;
    private BidDto bidDto;
    private String bidId;
    private Float bidAmount;
    private ResponseEntity<BidDto> responseBid;
    private HttpClientErrorException.BadRequest responseError;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AdRepository adRep;

    @Autowired
    private BidRepository bidRep;

    private String bidsEndpoint() {
        final String serverUrl = "http://localhost";
        final String bidsEndpoint = "/bids";
        return serverUrl + ":" + port + bidsEndpoint;
    }

    private void createLoggedInUser() {
        this.bidCreatorEntity = new Account("BidCreator", "bidcreator@gmail.com", "Bid Creator", "Password123!", true);
        this.bidCreatorEntity = accountRepository.save(this.bidCreatorEntity);
    }

    private void createAd() {
        AccountDto adCreatorDto = new AccountDto("AdCreator", "adcreator@gmail.com", "Ad Creator", "Password123!");
        this.adCreatorEntity = new Account("AdCreator", "adcreator@gmail.com", "Ad Creator", "Password123!", false);
        accountRepository.save(this.adCreatorEntity);

        AdDto ad = new AdDto();

        ad.setAddress("123 Wash-it Street");
        ad.setPickupDate(LocalDate.of(2021, 1, 1));
        ad.setPickupTimeStart(LocalTime.of(10, 0));
        ad.setPickupTimeEnd(LocalTime.of(11, 0));
        ad.setDropoffDate(LocalDate.of(2021, 1, 1));
        ad.setDropoffTimeStart(LocalTime.of(16, 0));
        ad.setDropoffTimeEnd(LocalTime.of(17, 0));
        ad.setWeightKg(10.0F);
        ad.setZipcode("H2X2B8");
        ad.setClothingDescription("Test");
        ad.setBleach(false);
        ad.setIron(false);
        ad.setFold(false);
        ad.setPhoneNumber("012-345-6789");
        ad.setCreator(adCreatorDto);
        ad.setSpecialInstructions("Test");
        ad.setLaundryStatus(LaundryStatus.POSTED);

        this.adDto = ad;
        this.adEntity = Ad.from(this.adDto);

        this.adEntity = adRep.save(this.adEntity);

        this.adId = this.adEntity.getId();
    }

    @Given("^I am a user$")
    public void i_am_a_user() {
        createLoggedInUser();
    }

    @And("^I am logged into my account$")
    public void i_am_logged_into_my_account() {
        Boolean isLoggedIn = accountRepository.findByUsername(this.bidCreatorEntity.getUsername()).getLoggedIn();
        assertEquals(true, isLoggedIn);
    }

    @And("^there exists an ad that is not posted by me$")
    public void there_exists_an_ad_that_is_not_posted_by_me() {
        createAd();
    }

    @When("^I try to make a bid of a 100 dollars on that ad$")
    public void i_try_to_make_a_bid_of_a_100_dollars_on_that_ad() {
        this.bidDto = new BidDto();
        bidDto.setAdId(this.adId);
        bidDto.setUsername(this.bidCreatorEntity.getUsername());
        bidDto.setAmount(100.0F);
        bidDto.setDateTimeCreated(LocalDateTime.now());
        bidDto.setIsAccepted(false);

        String token = jwtUtil.generateToken(this.bidCreatorEntity.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<BidDto> request = new HttpEntity<>(bidDto, headers);

        this.responseBid = restTemplate.postForEntity(bidsEndpoint(), request, BidDto.class);
        final BidDto bidDto = responseBid.getBody();
        if(responseBid.getStatusCode().equals(HttpStatus.CREATED)) {
            this.bidId = bidDto.getUsername();
            this.bidAmount = bidDto.getAmount();
        }
    }

    @Then("^that ad will have a bid of a 100 dollars$")
    public void that_ad_will_have_a_bid_of_a_100_dollars() {
        Float expectedBidAmount = 100.0F;
        assertEquals(expectedBidAmount, this.bidAmount);
    }

    @Given("^I posted an ad$")
    public void i_posted_an_ad() {
        // Delete the old ad created by adCreator
        if (this.adEntity != null) {
            adRep.delete(this.adEntity);
            this.adEntity = null;
        }

        // Create the new ad created by the bidCreator
        AccountDto creator = new AccountDto("BidCreator", "bidcreator@gmail.com", "Bid Creator", "Password123!");

        this.adDto.setCreator(creator);

        this.adEntity = Ad.from(this.adDto);

        this.adEntity = adRep.save(this.adEntity);

        this.adId = this.adEntity.getId();
    }

    @When("^I try to bid on that ad$")
    public void i_try_to_bid_on_that_ad() {
        this.bidDto = new BidDto();
        bidDto.setAdId(this.adId);
        bidDto.setUsername(this.bidCreatorEntity.getUsername());
        bidDto.setAmount(100.0F);
        bidDto.setDateTimeCreated(LocalDateTime.now());
        bidDto.setIsAccepted(false);

        String token = jwtUtil.generateToken(this.bidCreatorEntity.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<BidDto> request = new HttpEntity<>(bidDto, headers);

        this.responseError = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForEntity(
                        bidsEndpoint(), request, BidDto.class
                )
        );
    }

    @Then("^the system shall display an error$")
    public void the_system_shall_display_an_error_message() {
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
    }

    @After
    public void cleanUpCreateBid() {
        if (this.bidCreatorEntity != null) {
            List<Bid> postedBids = bidRep.findByCreator(this.bidCreatorEntity);
            if (postedBids.isEmpty() == false) {
                for (Bid bid : postedBids) {
                    bidRep.delete(bid);
                }
            }
        }

        if (this.adEntity != null) {
            adRep.delete(this.adEntity);
            this.adEntity = null;
        }

        if (this.bidCreatorEntity != null) {
            accountRepository.delete(this.bidCreatorEntity);
        }
        if (this.adCreatorEntity != null) {
            accountRepository.delete(this.adCreatorEntity);
        }
    }
}
