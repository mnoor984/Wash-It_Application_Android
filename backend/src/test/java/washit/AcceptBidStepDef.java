package washit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.entity.LaundryStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AcceptBidStepDef {

	 @LocalServerPort
	 private int port;
	 private final RestTemplate restTemplate = new RestTemplate();
	 private ResponseEntity<BidDto> responseBid;
	 private HttpClientErrorException.BadRequest responseError;
	 private Account account;
	 private Account accountBidder;
	 private Ad ad;
	 private Bid bid1;
	 private Bid bid2;
	 
	 @Autowired
	 private AccountRepository accountRepository;
	 
	 @Autowired
	 private AdRepository adRepository;
	 
	 @Autowired
	 private BidRepository bidRepository;
	 
	 private String bidsEndpoint() {
		 final String serverUrl = "http://localhost";
		 final String bidsEndpoint = "/bids";
	     return serverUrl + ":" + port + bidsEndpoint;
	 }
	 
	 @Given("I am a user of Wash-it")
	 public void i_am_a_user_of_wash_it(){
		 account = new Account("AcceptBidUser", "acceptbid101!@gmail.com", "Accepting Bidder", "Password123!", false);
		 account = accountRepository.save(account);
	 }
	 
	 @And("I logged into my account")
	 public void i_logged_into_my_account() {
		 account.setLoggedIn(true);
		 account = accountRepository.save(account);
	 }
	 
	 @And("I have posted a ad")
	 public void i_have_posted_a_ad() {
		 ad = new Ad();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		 LocalDateTime pickupStart = LocalDateTime.parse("2021-02-10T10:00", formatter);
	     LocalDateTime pickupEnd = LocalDateTime.parse("2021-02-10T11:00", formatter);
	     LocalDateTime dropoffStart = LocalDateTime.parse("2021-02-10T15:00", formatter);
	     LocalDateTime dropoffEnd = LocalDateTime.parse("2021-02-10T16:00", formatter);
	     
		 ad.setAddress("77777 Pepepga Street");
		 ad.setZipcode("HHHWWW");
		 ad.setPickupDate(pickupStart.toLocalDate());
		 ad.setDropoffDate(dropoffStart.toLocalDate());
		 ad.setPickupTimeStart(pickupStart.toLocalTime());
		 ad.setPickupTimeEnd(pickupEnd.toLocalTime());
		 ad.setDropoffTimeStart(dropoffStart.toLocalTime());
		 ad.setDropoffTimeEnd(dropoffEnd.toLocalTime());
		 ad.setClothingDescription("101 Shirts");
		 ad.setWeightKg(6.9f);
		 ad.setSpecialInstructions("No special instructions");
		 ad.setBleach(true);
		 ad.setIron(true);
		 ad.setFold(true);
		 ad.setLaundryStatus(LaundryStatus.POSTED);
		 ad.setPhoneNumber("777-777-7777");
		 ad.setCreator(account);
		 
		 ad = adRepository.save(ad);
	 }
	 
	 @And("there are at least two bids on my ad")
	 public void there_are_at_least_two_bids_on_my_ad() {
		 accountBidder = new Account("AcceptBidBidder", "acceptbidbidder@gmail.com", "Accepting Bid Bidder", "Password123!", false);
		 accountBidder = accountRepository.save(accountBidder);
		 
		 bid1 = new Bid();
		 bid1.setAdId(ad);
		 bid2 = new Bid();
		 bid2.setAdId(ad);
		 
		 bid1.setCreator(accountBidder);
		 bid1.setBidAmount(100.0F);
		 bid1.setDateTimeCreated(LocalDateTime.now());
		 bid1.setIsAccepted(false);
		 bid2.setCreator(accountBidder);
		 bid2.setBidAmount(200.0F);
		 bid2.setDateTimeCreated(LocalDateTime.now());
		 bid2.setIsAccepted(false);
		 
		 bidRepository.save(bid1);
		 bidRepository.save(bid2);
	 }
	 
	 @When("I accept a bid on my ad")
	 public void i_accept_a_bid_on_my_ad() {
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 responseBid = restTemplate.exchange(
			      bidsEndpoint() + "/{adId}/{username}", HttpMethod.PUT, new HttpEntity<>(BidDto.from(bid1), headers), BidDto.class, ad.getId(), accountBidder.getUsername()
		 );
	 }
	 
	 @Then("that ad will have an accepted bid")
	 public void that_ad_will_have_an_accepted_bid() {
		 BidDto bid = responseBid.getBody();
		 assertEquals(true, bid.getIsAccepted());
		 assertEquals(HttpStatus.OK, responseBid.getStatusCode());
		 cleanUp();
	 }
	 
	 @Given("I already have an accepted bid on my ad")
	 public void i_already_have_an_accepted_bid_on_my_ad() {
		 bid1.setIsAccepted(true);
		 bidRepository.save(bid1);
	 }
	 
	 @When("I try to accept another bid")
	 public void i_try_to_accept_another_bid() {
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 responseError = assertThrows(
				 HttpClientErrorException.BadRequest.class,
				 () -> restTemplate.exchange(
						 bidsEndpoint() + "/{adId}/{username}", HttpMethod.PUT, new HttpEntity<>(BidDto.from(bid2), headers), BidDto.class, ad.getId(), accountBidder.getUsername()
				 )
		 );
	 }
	 
	 @Then("an error message {string} will be displayed")
	 public void an_error_message_will_be_displayed(String aString) {
		 assertNotNull(responseError);
		 assertEquals(HttpStatus.BAD_REQUEST, responseError.getStatusCode());
		 assertEquals(aString, responseError.getResponseBodyAsString());
		 cleanUp();
	 }
	 
	 public void cleanUp() {
		 if(bid1 != null) {
			 bidRepository.delete(bid1);
		 }
		 if(bid2 != null) {
			 bidRepository.delete(bid2);
		 }
		 if(ad != null) {
			 adRepository.deleteById(ad.getId());
			 ad = null;
		 }
		 if(accountBidder != null) {
			 accountRepository.delete(accountBidder);
		 }
		 if(account != null) {
			 accountRepository.delete(account);
		 }
	 }
	    
}
