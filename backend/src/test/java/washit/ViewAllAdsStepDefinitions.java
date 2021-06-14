package washit;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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
import washit.util.JwtUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ViewAllAdsStepDefinitions {

  @LocalServerPort
  private int port;
  private final RestTemplate restTemplate = new RestTemplate();
  private String customer;
  private AdRep ad;
  private ArrayList<String> idArray = new ArrayList<>();

  @Autowired
  JwtUtil jwtUtil;

  private ResponseEntity<AdRep> responseAd;
  private ResponseEntity<AdRep[]> responseAllAds;
  private HttpClientErrorException.BadRequest responseError;
  private int numberOfAdsCreated = 0;
  private Account testAccount;

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private AdRepository adRepository;


  private String adsEndpoint() {
    final String serverUrl = "http://localhost";
    final String adsEndpoint = "/ads";
    return serverUrl + ":" + port + adsEndpoint;
  }

  private String accountsEndpoint() {
    final String serverUrl = "http://localhost";
        final String accountEndpoint = "/accounts";
        return serverUrl + ":" + port + accountEndpoint;
    }

    
    private AdRep createAd() {
    	
    	testAccount = new Account("ViewAdsTestAccount", "viewAds@gmail.com", "view ad", "password", true);
    	testAccount = accountRepository.save(testAccount);

        AdRep ad = new AdRep();
        
        ad.setAccount(testAccount.getUsername());
        ad.setPhoneNum("514-555-4444");
        ad.setClothingDesc("1 pijama, 3 hoodies");
        ad.setWeight(Float.valueOf(1.0f));
        ad.setPickupStart("2021-02-09T10:00");
        ad.setPickupEnd("2021-02-09T11:00");
        ad.setDropoffStart("2021-02-09T16:00");
        ad.setDropoffEnd("2021-02-09T17:00");
        ad.setAddress("3415 Girouard");
        ad.setZipcode("H2X2B8");
        ad.setBleach(Boolean.FALSE);
        ad.setIron(Boolean.FALSE);
        ad.setFold(Boolean.FALSE);
        ad.setSpecialInst("careful with my pijama");

        return ad;
    }

	@Given("an ad with UID {int} is created")
	public void an_ad_with_uid_is_created(Integer int1) {
    this.ad = createAd();

    // jwt
    String token = jwtUtil.generateToken("ViewAdsTestAccount");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(token);
    HttpEntity<AdRep> request = new HttpEntity<>(ad, headers);

    this.responseAd = restTemplate.postForEntity(adsEndpoint(), request, AdRep.class);


    final AdRep adResp = responseAd.getBody();
        if(responseAd.getStatusCode().equals(HttpStatus.OK)) {
        	++numberOfAdsCreated;
        	idArray.add(adResp.getId());
        }
	}
	
	@When("I view ads")
	public void i_view_ads() throws URISyntaxException {
		
        URI getAllAds = new URI(adsEndpoint()); 
        this.responseAllAds = restTemplate.getForEntity(getAllAds, AdRep[].class);
	}
	
	@Then("two ads with UID {int} and UID {int} should be returned")
	public void two_ads_with_uid_and_uid_should_be_returned(Integer int1, Integer int2) {
		
		AdRep[] ads = responseAllAds.getBody();
		assertNotNull(ads);
        assertEquals(HttpStatus.OK, responseAllAds.getStatusCode());
        
        ArrayList<String> retrievedIds = new ArrayList<>();
		for (int i = 0 ; i < ads.length ; ++i) {
			retrievedIds.add(ads[i].getId());
		}
		
		for (String id : idArray ) {
			assertTrue(retrievedIds.contains(id));
		}
		
		assertTrue(numberOfAdsCreated ==2);

	}
	
	@Then("one ad with UID {int} should be returned")
	public void one_ad_with_uid_should_be_returned(Integer int1) {
		
		
		AdRep[] ads = responseAllAds.getBody();
		assertNotNull(ads);
        assertEquals(HttpStatus.OK, responseAllAds.getStatusCode());
        
        ArrayList<String> retrievedIds = new ArrayList<>();
		for (int i = 0 ; i < ads.length ; ++i) {
			retrievedIds.add(ads[i].getId());
		}
		
		for (String id : idArray ) {
			assertTrue(retrievedIds.contains(id));
		}
		assertTrue(numberOfAdsCreated ==1);
	}
	
	@Then("a {string} error message is issued")
	public void a_error_message_is_issued(String string) {
	 
		AdRep[] ads = responseAllAds.getBody();
		assertNotNull(ads);
        assertEquals(HttpStatus.OK, responseAllAds.getStatusCode());
        assertEquals(numberOfAdsCreated, 0);
	}
	
	@After
	public void cleanUp(){
		for (String id : idArray) {
      adRepository.deleteById(Integer.parseInt(id));
		}
		if(numberOfAdsCreated != 0) {
      accountRepository.deleteById(testAccount.getUsername());
    }
		numberOfAdsCreated = 0;
		idArray.clear();
	}


}
