package washit;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.RestTemplate;
import washit.controller.representation.AdRep;
import washit.controller.representation.BidRep;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.service.exception.BidException;
import washit.util.JwtUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ViewAllBidsStepDefinitions {

	@LocalServerPort
    private int port;
    private AdRep ad;
    private int adsCreated = 0;
	private final RestTemplate restTemplate = new RestTemplate();
    private Account testAccount;
    private Account testAccount2;
    private int accountsCreated = 0;
    private BidDto bid;
    private ArrayList<String> adArray = new ArrayList<>();
	private ResponseEntity<BidDto> responseBid;
	private ResponseEntity<BidDto> responseBid2;
	private ResponseEntity<AdRep> responseAd;
	private ArrayList<String> idArray = new ArrayList<>();
	private ResponseEntity<BidDto[]> responseAllBids;
	private int numberOfBidsCreated=0;
	private HttpStatus responseError;
	@Autowired
	private JwtUtil jwtUtil;
   
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AdRepository adRepository;
 
	
	 
	 private String bidsEndpoint() {
	        final String serverUrl = "http://localhost";
	        final String bidsEndpoint = "/bids";
	        return serverUrl + ":" + port + bidsEndpoint;
	 }
	  private String adsEndpoint() {
		    final String serverUrl = "http://localhost";
		    final String adsEndpoint = "/ads";
		    return serverUrl + ":" + port + adsEndpoint;
		  }
	 
	 private Account createLoggedInUser() {
	    	this.testAccount = new Account("BidsTestAccount", "viewBids@gmail.com", "view bid", "password", true);
	    	this.testAccount = accountRepository.save(this.testAccount);
	    	return this.testAccount;
	 }
	 private AdRep createAd() {
	    	
	    	this.testAccount2 = new Account("TestAccountViewBids", "viewBiddsss@gmail.com", "view bids", "password", true);
	    	this.testAccount2 = accountRepository.save(this.testAccount2);
	    	accountsCreated++;
	        AdRep ad = new AdRep();
	        
	        ad.setAccount(testAccount2.getUsername());
	        ad.setPhoneNum("514-555-4444");
	        ad.setClothingDesc("1 pijama, 3 hoodies");
	        ad.setWeight(Float.valueOf(1.0f));
	        ad.setPickupStart("2021-02-09T10:00");
	        ad.setPickupEnd("2021-02-09T11:00");
	        ad.setDropoffStart("2021-02-09T16:00");
	        ad.setDropoffEnd("2021-02-09T17:00");
	        ad.setAddress("3415 Giroud");
	        ad.setZipcode("H2X2B8");
	        ad.setBleach(Boolean.FALSE);
	        ad.setIron(Boolean.FALSE);
	        ad.setFold(Boolean.FALSE);
	        ad.setSpecialInst("careful with my pijama");

	        return ad;
	 }
	 
	 @Given("I am currently logged in as a Wash-It user")
	 public void i_am_currently_logged_in_as_a_user() {
		 createLoggedInUser();
	 }
	 
	 @And("there is an ad with UID {int} with no bids")
	 public void there_is_an_ad_with_uid_with_no_bids(Integer id) {
		 this.ad = createAd();

		  // jwt
		  String token = jwtUtil.generateToken("TestAccountViewBids");

		  HttpHeaders headers = new HttpHeaders();
		  headers.setContentType(MediaType.APPLICATION_JSON);
		  headers.setBearerAuth(token);
		  HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

		  this.responseAd = restTemplate.postForEntity(adsEndpoint(), request, AdRep.class);
		  final AdRep adResp = responseAd.getBody();
	        if(responseAd.getStatusCode().equals(HttpStatus.OK)) {
	        	adArray.add(adResp.getId());
	        	adsCreated++;

	        }
	 } 
	 
	 @Given("a bid is made on the ad with UID {int}")
	 public void a_bid_is_made_on_this_ad_with_uid(Integer id) {
		 bid = new BidDto();
		 bid.setAdId(Integer.parseInt(responseAd.getBody().getId()));
		 bid.setUsername(this.testAccount.getUsername());
		 bid.setAmount((float) 100);
		 bid.setDateTimeCreated(LocalDateTime.of(2015, 
                 Month.JULY, 29, 19, 30, 40));
		 bid.setIsAccepted(false);
		 
		 String token = jwtUtil.generateToken("BidsTestAccount");

		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 headers.setBearerAuth(token);
		 HttpEntity<BidDto> request = new HttpEntity<>(bid, headers);

		 this.responseBid = restTemplate.postForEntity(bidsEndpoint(), request, BidDto.class);
		 final BidDto bidDto = responseBid.getBody();
	        if(responseBid.getStatusCode().equals(HttpStatus.CREATED)) {
	        	++numberOfBidsCreated;
	        	idArray.add(bidDto.getUsername());
	        }
	 }
	 
	 @And("another bid on the ad with UID {int} is created")
	 public void another_bid_is_made_on_this_ad_with_uid(Integer id) {
		 bid = new BidDto();
		 bid.setAdId(Integer.parseInt(responseAd.getBody().getId()));
		 bid.setUsername("RegularCustomer");
		 bid.setAmount((float) 150);
		 bid.setDateTimeCreated(LocalDateTime.of(2015, 
                 Month.JULY, 30, 19, 30, 40));
		 bid.setIsAccepted(false);
		 
		 String token = jwtUtil.generateToken("RegularCustomer");

		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.APPLICATION_JSON);
		 headers.setBearerAuth(token);
		 HttpEntity<BidDto> request = new HttpEntity<>(bid, headers);

		 this.responseBid2 = restTemplate.postForEntity(bidsEndpoint(), request, BidDto.class);
		 final BidDto bidDto = responseBid2.getBody();
	        if(responseBid2.getStatusCode().equals(HttpStatus.CREATED)) {
	        	++numberOfBidsCreated;
	        	idArray.add(bidDto.getUsername());
	        }
	 }
	 
	 @When("I view bids on the ad with UID {int}")
	 public void i_view_bids_on_this_ad_with_uid(Integer id) throws URISyntaxException {
		 final String serverUrl = "http://localhost";
	     final String bidsEndpoint = "/bids";
	     String uri = (serverUrl + ":" + port + bidsEndpoint + "/" +Integer.parseInt(responseAd.getBody().getId()));
		 
	     try {
		     URI getAllBids = new URI(uri);
		     this.responseAllBids = restTemplate.getForEntity(getAllBids, BidDto[].class);

	     }
	     catch(BadRequest e) {
	    	 responseError=e.getStatusCode();
	    	 
	     }
	 }
	 
	 @Then("the two bids should be returned")
	 public void the_two_bids_should_be_returned() {
		 BidDto[] bids = responseAllBids.getBody();
		 System.out.println(bids);
		 assertNotNull(bids);
	     assertEquals(HttpStatus.OK, responseAllBids.getStatusCode());
	        ArrayList<String> retrievedIds = new ArrayList<>();
			for (int i = 0 ; i < bids.length ; ++i) {
				retrievedIds.add(bids[i].getUsername());
			}
			
			for (String id : idArray) {
				assertTrue(retrievedIds.contains(id));
			}
			assertEquals(numberOfBidsCreated, bids.length);

	 }
	 
	 @Then("one bid should be returned")
	 public void one_bid_should_be_returned() {
		 BidDto[] bids = responseAllBids.getBody();
		 assertNotNull(bids);
	     assertEquals(HttpStatus.OK, responseAllBids.getStatusCode());
	     
	        ArrayList<String> retrievedIds = new ArrayList<>();
			for (int i = 0 ; i < bids.length ; ++i) {
				retrievedIds.add(bids[i].getUsername());
			}
			
			for (String id : idArray) {
				assertTrue(retrievedIds.contains(id));
			}
			assertEquals(numberOfBidsCreated, bids.length);

	 }
	 
	 @Then("a {string} message is issued")
	 public void a_message_is_issued(String string) {
		assertNull(responseAllBids);
	    assertEquals(HttpStatus.BAD_REQUEST, responseError);
	    assertEquals(numberOfBidsCreated, 0);
	 }
	 
	@After
	public void cleanUp() throws URISyntaxException{
		if(adsCreated != 0){
			for (String id : adArray) {
				 adRepository.deleteById(Integer.parseInt(id));
			}
		}
		numberOfBidsCreated = 0;
		adsCreated = 0;
		if(accountsCreated != 0) {
			accountRepository.deleteById(testAccount2.getUsername());
		}
		idArray.clear();
		responseError = null;

	}
}
