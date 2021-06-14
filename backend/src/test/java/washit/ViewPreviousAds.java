package washit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

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
import washit.entity.Ad;
import washit.service.exception.AdException;
import washit.util.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ViewPreviousAds {

	@LocalServerPort
	private int port;
	private final RestTemplate restTemplate = new RestTemplate();
	private ArrayList<String> usersCreatedArray = new ArrayList<>();
	private ArrayList<Integer> adsCreatedArray = new ArrayList<>();
	private URI getAdsOfUser;

	@Autowired
	JwtUtil jwtUtil;

	private ResponseEntity<AdRep[]> responseAllAds;
	private Account testAccount;
	private String error;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private AdRepository adRepository;

	private String adsEndpoint() {
		final String serverUrl = "http://localhost";
		final String adsEndpoint = "/ads/account";
		return serverUrl + ":" + port + adsEndpoint;
	}

	@Given("that there is an acount for Wash-It with the username {string}")
	public void that_there_is_an_acount_for_wash_it_with_the_username(String string) {
		testAccount = new Account(string, "viewAds@gmail.com", "view ad", "password", true);
		testAccount = accountRepository.save(testAccount);
		usersCreatedArray.add(testAccount.getUsername());
	}

	@Given("that the account with username {string} initially has no ads")
	public void that_the_account_with_username_initially_has_no_ads(String string) {

	}

	@Given("that the account with username {string} posts an Ad with adId {int}")
	public void that_the_account_with_username_posts_an_ad_with_ad_id(String string, Integer int1) {
		Account anAccount = accountRepository.findById(string).get();
		Ad anAd = new Ad();
		anAd.setCreator(anAccount);
		anAd.setPhoneNumber("514-555-4444");
		anAd.setClothingDescription("1 pijama, 3 hoodies");
		anAd.setWeightKg(Float.valueOf(1.0f));
		anAd.setPickupDate(LocalDate.of(2021, 3, 12));
		anAd.setPickupTimeStart(LocalTime.of(10, 30));
		anAd.setPickupTimeEnd(LocalTime.of(12, 30));
		anAd.setDropoffDate(LocalDate.of(2021, 3, 12));
		anAd.setDropoffTimeStart(LocalTime.of(17, 30));
		anAd.setDropoffTimeEnd(LocalTime.of(18, 30));
		anAd.setAddress("3415 Girouard");
		anAd.setZipcode("H2X2B8");
		anAd.setBleach(Boolean.FALSE);
		anAd.setIron(Boolean.FALSE);
		anAd.setFold(Boolean.FALSE);
		anAd.setSpecialInstructions("careful with my pijama");

		adRepository.save(anAd);
		adsCreatedArray.add(anAd.getId());

	}

	@Given("that there is not an account for Wash-It with the name {string}")
	public void that_there_is_not_an_account_for_wash_it_with_the_name(String string) {

	}

	@When("I request to view ads of an user account")
	public void i_request_to_view_ads_of_an_user_account() throws URISyntaxException {

	}

	@When("I supply {string} as the username")
	public void i_supply_as_the_username(String string) throws URISyntaxException {
		getAdsOfUser = new URI(adsEndpoint() + "/" + string);
		try {
			this.responseAllAds = restTemplate.getForEntity(getAdsOfUser, AdRep[].class);
		} catch (HttpClientErrorException e) {
			error = e.getResponseBodyAsString();
		}

	}

	@Then("the ads with adIds {int} and {int} are returned")
	public void the_ads_with_ad_ids_and_are_returned(Integer int1, Integer int2) {
		AdRep[] ads = responseAllAds.getBody();
		assertNotNull(ads);
		assertEquals(HttpStatus.OK, responseAllAds.getStatusCode());

		ArrayList<String> retrievedIds = new ArrayList<>();
		for (int i = 0; i < ads.length; ++i) {
			retrievedIds.add(ads[i].getId());
		}

		for (Integer id : adsCreatedArray) {
			String idAsString = String.valueOf(id);
			assertTrue(retrievedIds.contains(idAsString));
		}

		assertTrue(ads.length == 2);
	}

	@Then("no ads are returned")
	public void no_ads_are_returned() {
		if (responseAllAds != null) {
			assertNull(responseAllAds);
		}
		
	}

	@Then("the message {string} is returned")
	public void the_message_is_returned(String string) {
		assertEquals(string, error);
	}

	@After
	public void cleanUp() {
		for (Integer id : adsCreatedArray) {
			adRepository.deleteById(id);
		}
		for (String username : usersCreatedArray) {
			accountRepository.deleteById(username);
		}
		adsCreatedArray.clear();
		usersCreatedArray.clear();
	}
}
