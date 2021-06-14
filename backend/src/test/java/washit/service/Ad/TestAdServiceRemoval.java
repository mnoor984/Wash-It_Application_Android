package washit.service.Ad;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dto.AdDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.LaundryStatus;
import washit.service.AdService;
import washit.service.AdServiceImpl;
import washit.service.exception.AdException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;


public class TestAdServiceRemoval {

	private final AdRepository adRepo = Mockito.mock(AdRepository.class);
	private final AccountRepository accRepo = Mockito.mock(AccountRepository.class);
	private AdService adService;
	
	private static final Integer EXISTING_ID = 2;
	private static final Integer EXISTING_ID_NOT_LOGGED_IN = 3;
	private static final Integer NONEXISTING_ID = 4;
	private static final Integer EXISTING_ID_INPROGRESS = 5;
	private static final String VALID_USERNAME = "ValidUserName";
	private static final String VALID_FULLNAME = "John Smith";
	private static final String VALID_EMAIL="johnsmith@gmail.com";
	private static final String VALID_PASSWORD="Ecse321@000";

	
	@BeforeEach
	public void setMockOutput() {
		adService = new AdServiceImpl(adRepo,accRepo);
		
		lenient().when(adRepo.existsById(anyInt())).thenAnswer((InvocationOnMock invocation)-> {
			if (invocation.getArgument(0).equals(EXISTING_ID)) {
				return true;
			}
			if (invocation.getArgument(0).equals(EXISTING_ID_NOT_LOGGED_IN)) {
				return true;
			}
			return invocation.getArgument(0).equals(EXISTING_ID_INPROGRESS);
		});
		
		lenient().when(adRepo.findAdById(anyInt())).thenAnswer((InvocationOnMock invocation)-> {
			if (invocation.getArgument(0).equals(NONEXISTING_ID)) {
				return Optional.empty();
			}
			if (invocation.getArgument(0).equals(EXISTING_ID)) {

				Optional<Ad> ad = Optional.of(new Ad());
				Account creator = new Account(VALID_USERNAME, VALID_EMAIL, VALID_FULLNAME, VALID_PASSWORD, true);
				ad.get().setCreator(creator);
				ad.get().setId(EXISTING_ID);
				creator.setLoggedIn(true);
				return ad;
			}
			if (invocation.getArgument(0).equals(EXISTING_ID_NOT_LOGGED_IN)) {
				Optional<Ad> ad = Optional.of(new Ad());
				Account creator = new Account(VALID_USERNAME, VALID_EMAIL, VALID_FULLNAME, VALID_PASSWORD, false);
				ad.get().setCreator(creator);
				ad.get().setId(EXISTING_ID_NOT_LOGGED_IN);
				creator.setLoggedIn(false);
				return ad;
			}
			if (invocation.getArgument(0).equals(EXISTING_ID_INPROGRESS)) {
				Optional<Ad> ad = Optional.of(new Ad());
				Account creator = new Account(VALID_USERNAME, VALID_EMAIL, VALID_FULLNAME, VALID_PASSWORD, true);
				ad.get().setCreator(creator);
				ad.get().setId(EXISTING_ID_INPROGRESS);
				creator.setLoggedIn(true);
				ad.get().setLaundryStatus(LaundryStatus.INPROGRESS);
				return ad;
			}
			else {
				return false;
			}
		});
		
		lenient().doAnswer((i)->null).when(adRepo).delete(any(Ad.class));

	}
	
	@Test
	public void removeAdSuccessfully() {
		AdDto adDto = null;
		try {
			adDto = adService.removeAd(EXISTING_ID);
		} catch (AdException e) {
			fail();
		}
		assertNotNull(adDto);
		assertEquals(EXISTING_ID, adDto.getId());
	}
	
	@Test
	public void removeAdNullId() {
		AdDto adDto = null;
		String error = "";
		try {
			adDto = adService.removeAd(null);
		} catch (AdException e) {
			error = e.getMessage();
		}
		assertNull(adDto);
		assertEquals("Null Id. Imposible to remove Ad", error);
	}
	
	@Test
	public void removeAdNonExistentId() {
		AdDto adDto = null;
		String error = "";
		try {
			adDto = adService.removeAd(NONEXISTING_ID);
		} catch (AdException e) {
			error = e.getMessage();
		}
		assertNull(adDto);
		assertEquals("No ad was found under the provided id", error);
	}

	@Test
	public void removeAdWithBidAccepted() {
		AdDto adDto = null;
		String error = "";
		try {
			adDto = adService.removeAd(EXISTING_ID_INPROGRESS);
		} catch (AdException e) {
			error = e.getMessage();
		}
		assertNull(adDto);
		assertEquals("Washing process is already in progress. Impossible to delete ad", error);
	}
}
