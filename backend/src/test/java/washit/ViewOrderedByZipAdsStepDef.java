package washit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import washit.controller.representation.AdRep;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.entity.Account;
import washit.util.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ViewOrderedByZipAdsStepDef {

	@LocalServerPort
	private int port;
	private final RestTemplate restTemplate = new RestTemplate();
	private ArrayList<String> sortedZipCodes = new ArrayList<>();
	private URI getAdsByZip;

	@Autowired
	JwtUtil jwtUtil;

	private ResponseEntity<AdRep[]> responseAllAdsByZip;
	private ResponseEntity<AdRep[]> responseAllAds;
	private String error;


	private String adsEndpoint() {
		final String serverUrl = "http://localhost";
		final String adsEndpoint = "/ads/asc";
		return serverUrl + ":" + port + adsEndpoint;
	}
	
	private String adsEndpointUnsorted() {
		final String serverUrl = "http://localhost";
		final String adsEndpoint = "/ads";
		return serverUrl + ":" + port + adsEndpoint;
	}
	
	@Given("I am viewing all the ads in the system")
	public void i_am_viewing_all_the_ads_in_the_system() throws URISyntaxException {
		getAdsByZip = new URI(adsEndpoint());
	}

	@When("I choose the option to sort ads by postal code")
	public void i_choose_the_option_to_sort_ads_by_postal_code() {
		try {
			this.responseAllAdsByZip = restTemplate.getForEntity(getAdsByZip, AdRep[].class);
		} catch (HttpClientErrorException e) {
			error = e.getResponseBodyAsString();
		}
	}
	@Then("A list of ads, sorted by postal code, is given to me")
	public void a_list_of_ads_sorted_by_postal_code_is_given_to_me() {
		AdRep[] ads = responseAllAdsByZip.getBody();
		assertNotNull(ads);
		assertEquals(HttpStatus.OK, responseAllAdsByZip.getStatusCode());

		ArrayList<String> retrievedZip = new ArrayList<>();
		
		for(int i = 0 ; i < ads.length ; ++i) {
			retrievedZip.add(ads[i].getZipcode());
		}
		
		Collections.sort(retrievedZip);
		for(int i = 0 ; i < ads.length ; ++i) {
			assertEquals(ads[i].getZipcode(), retrievedZip.get(i));
		}
	}
	
	@Given("they are sorted by postal code")
	public void they_are_sorted_by_postal_code() {
		
		try {
			this.responseAllAdsByZip = restTemplate.getForEntity(getAdsByZip, AdRep[].class);
		} catch (HttpClientErrorException e) {
			error = e.getResponseBodyAsString();
		}
		
		AdRep[] ads = responseAllAdsByZip.getBody();
		
		for(int i = 0 ; i < ads.length ; ++i) {
			sortedZipCodes.add(ads[i].getZipcode());
		}
	}

	@When("I choose the option to unsort ads by postal code")
	public void i_choose_the_option_to_unsort_ads_by_postal_code() throws URISyntaxException {
		URI unsortedAds = new URI (adsEndpointUnsorted());
		
		try {
			this.responseAllAds = restTemplate.getForEntity(unsortedAds, AdRep[].class);
		} catch (HttpClientErrorException e) {
			error = e.getResponseBodyAsString();
		}
	}
	@Then("A list of ads, sorted by most recent, is given to me")
	public void a_list_of_ads_sorted_by_most_recent_is_given_to_me() {
		AdRep[] ads = responseAllAds.getBody();
		assertNotNull(ads);
		assertEquals(HttpStatus.OK, responseAllAds.getStatusCode());
		
		ArrayList<Integer> retrievedIds = new ArrayList<>();
		for (int i = 0; i < ads.length; ++i) {
			retrievedIds.add(Integer.parseInt(ads[i].getId()));
		}
		
		Collections.sort(retrievedIds);
		Collections.reverse(retrievedIds);
		
		for(int i = 0 ; i < ads.length ; ++i) {
			assertEquals(ads[i].getId(), retrievedIds.get(i).toString());
		}
		
	}
	
	@After
	public void cleanUp() {
		
		sortedZipCodes.clear();
		error = "";
	}
}
