package washit.service.Ad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import washit.dao.AdRepository;
import washit.dto.AdDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.LaundryStatus;
import washit.service.AdServiceImpl;
import washit.service.exception.AdException;

@ExtendWith(MockitoExtension.class)
public class TestAllAdsServiceAvailable {

	@Mock
	private AdRepository adRepo;
	
	@InjectMocks
	private AdServiceImpl service;
	
	private static final int VALID_ID = 10;
	private static final String VALID_ADDRESS = "1337 NoScope";
	private static final LocalDate VALID_PICKUP_DATE = LocalDate.of(2021, 3, 18);
	private static final LocalTime VALID_PICKUP_TIME_START = LocalTime.of(8, 45);
	private static final LocalTime VALID_PICKUP_TIME_END = LocalTime.of(12, 45);
	private static final LocalDate VALID_DROPOFF_DATE = LocalDate.of(2021, 3, 20);
	private static final LocalTime VALID_DROPOFF_TIME_START = LocalTime.of(7, 30);
	private static final LocalTime VALID_DROPOFF_TIME_END = LocalTime.of(11, 30);
	private static final Float VALID_WEIGHT = 6969f;
	private static final String VALID_ZIP = "WWW111";
	private static final Boolean VALID_BLEACH = true;
	private static final Boolean VALID_IRON = false;
	private static final Boolean VALID_FOLD = false;
	private static final String VALID_PHONE = "1230984576";
	private static final String VALID_DESCRIPTION = "clean my clothes plzzzzzz";
	private static final String VALID_EMAIL = "washme@gmail.com";
	private static final String VALID_USERNAME = "John123";
	private static final String VALID_FULLNAME = "John John";
	private static final String VALID_PASSWORD = "123John";
	
	@BeforeEach
	public void setMockOutput() {		
		lenient().when(adRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			
			ArrayList<Ad> list = new ArrayList<Ad>();
			Iterable<Ad> iterables = new ArrayList<Ad>();
			
			Account creator = new Account();
			creator.setEmail(VALID_EMAIL);
			creator.setFullName(VALID_FULLNAME);
			creator.setPassword(VALID_PASSWORD);
			creator.setUsername(VALID_USERNAME);
			
			for(int i = 9; i>=0; i--) {
				
				Ad ad = new Ad();
				ad.setId(VALID_ID-i);
				ad.setAddress(VALID_ADDRESS+i);
				ad.setPickupDate(VALID_PICKUP_DATE);
				ad.setPickupTimeStart(VALID_PICKUP_TIME_START);
				ad.setPickupTimeEnd(VALID_PICKUP_TIME_END);
				ad.setDropoffDate(VALID_DROPOFF_DATE);
				ad.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
				ad.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
				ad.setWeightKg(VALID_WEIGHT-i);
				ad.setZipcode(VALID_ZIP);
				ad.setBleach(VALID_BLEACH);
				ad.setIron(VALID_IRON);
				ad.setFold(VALID_FOLD);
				ad.setPhoneNumber(VALID_PHONE);
				ad.setClothingDescription(VALID_DESCRIPTION);
				if(i%2 == 0) ad.setLaundryStatus(LaundryStatus.POSTED);
				else  ad.setLaundryStatus(LaundryStatus.INPROGRESS);
				
				ad.setCreator(creator);
				
				list.add(ad);
				
			}
			
			iterables = (Iterable<Ad>) list;
			return iterables;
		});
	}
	
	@Test
	public void testAvailableGetAds() {
		
		List<AdDto> list = null;
		
		try {
			list = service.findAllAvailableAds();
			
		}catch(AdException e) {
			fail();
		}
		
		assertNotNull(list);
		for(int i=9; i>=0; i--) {
			if(i%2 == 0) {
				Float weight = VALID_WEIGHT - i;
				
				assertEquals((int) list.get((int) i/2).getId(), VALID_ID-i);
				assertEquals(list.get((int) i/2).getAddress(), VALID_ADDRESS+i);
				assertEquals(list.get((int) i/2).getPickupDate(), VALID_PICKUP_DATE);
				assertEquals(list.get((int) i/2).getPickupTimeStart(), VALID_PICKUP_TIME_START);
				assertEquals(list.get((int) i/2).getPickupTimeEnd(), VALID_PICKUP_TIME_END);
				assertEquals(list.get((int) i/2).getDropoffDate(), VALID_DROPOFF_DATE);
				assertEquals(list.get((int) i/2).getDropoffTimeStart(), VALID_DROPOFF_TIME_START);
				assertEquals(list.get((int) i/2).getDropoffTimeEnd(), VALID_DROPOFF_TIME_END);
				assertEquals(list.get((int) i/2).getWeightKg(), weight);
				assertEquals(list.get((int) i/2).getZipcode(), VALID_ZIP);
				assertEquals(list.get((int) i/2).getBleach(), VALID_BLEACH);
				assertEquals(list.get((int) i/2).getIron(), VALID_IRON);
				assertEquals(list.get((int) i/2).getFold(), VALID_FOLD);
				assertEquals(list.get((int) i/2).getPhoneNumber(), VALID_PHONE);
				assertEquals(list.get((int) i/2).getClothingDescription(), VALID_DESCRIPTION);
				assertEquals(list.get((int) i/2).getLaundryStatus(), LaundryStatus.POSTED);
				assertEquals(list.get((int) i/2).getCreator().getUsername(), VALID_USERNAME);
				
			}
		}
		
	}
	
}
