package washit.service.Ad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
public class TestAdServiceAdsByPostalCode {
	
	@Mock
	private AdRepository adRepo;
	
	@InjectMocks
	private AdServiceImpl service;
	
	private static final int ID = 1;
	private static final String ADDRESS = "6969 St. Pepega";
	private static final LocalDate PICKUP_DATE = LocalDate.of(2021, 3, 25);
	private static final LocalTime PICKUP_TIME_START = LocalTime.of(3, 25);
	private static final LocalTime PICKUP_TIME_END = LocalTime.of(7, 25);
	private static final LocalDate DROPOFF_DATE = LocalDate.of(2021, 3, 27);
	private static final LocalTime DROPOFF_TIME_START = LocalTime.of(7, 30);
	private static final LocalTime DROPOFF_TIME_END = LocalTime.of(11, 30);
	private static final Float WEIGHT = 1337f;
	private static final String ZIP = "A1A1A5";
	private static final Boolean BLEACH = true;
	private static final Boolean IRON = false;
	private static final Boolean FOLD = false;
	private static final String PHONE = "(514)-202-9999";
	private static final String DESCRIPTION = "what am I even doing?..";
	private static final String EMAIL = "email@gmail.com";
	private static final String USERNAME = "Habibi123";
	private static final String FULLNAME = "Habibi Don";
	private static final String PASSWORD = "Password101!";
	
	private static boolean isValid;
	private static boolean isAvailable;
	
	@BeforeEach
	public void setMockOut() {
		 lenient().when(adRepo.findAdsByOrderByZipcodeAsc()).thenAnswer((InvocationOnMock invocation)->{
			 ArrayList<Ad> list = new ArrayList<Ad>();
			 Iterable<Ad> iterables = new ArrayList<Ad>();
			 
			 if(isValid) {
				 Account creator = new Account();
				 creator.setEmail(EMAIL);
				 creator.setFullName(FULLNAME);
				 creator.setPassword(PASSWORD);
				 creator.setUsername(USERNAME);
				
				 for(int i = 1; i<5; i++) {
					 Ad ad = new Ad();
					 ad.setId(ID+i);
					 ad.setAddress(ADDRESS);
					 ad.setPickupDate(PICKUP_DATE);
					 ad.setPickupTimeStart(PICKUP_TIME_START);
					 ad.setPickupTimeEnd(PICKUP_TIME_END);
					 ad.setDropoffDate(DROPOFF_DATE);
					 ad.setDropoffTimeStart(DROPOFF_TIME_START);
					 ad.setDropoffTimeEnd(DROPOFF_TIME_END);
					 ad.setWeightKg(WEIGHT);
					 
					 int num = Integer.parseInt(ZIP.replace("A1A1A", ""))+i;
					 ad.setZipcode("A1A1A"+num);
					 
					 ad.setBleach(BLEACH);
					 ad.setIron(IRON);
					 ad.setFold(FOLD);
					 ad.setPhoneNumber(PHONE);
					 ad.setClothingDescription(DESCRIPTION);
					 if(isAvailable) ad.setLaundryStatus(LaundryStatus.POSTED);
					 else ad.setLaundryStatus(LaundryStatus.INPROGRESS);
					 ad.setCreator(creator);
					
					 list.add(ad);
				 }
			
			 }
			 	
			 iterables = (Iterable<Ad>) list;
			 return iterables;
		 });
		 
		 lenient().when(adRepo.findAdsByOrderByZipcodeDesc()).thenAnswer((InvocationOnMock invocation)->{
			 ArrayList<Ad> list = new ArrayList<Ad>();
			 Iterable<Ad> iterables = new ArrayList<Ad>();
				
			 if(isValid) {
				 Account creator = new Account();
				 creator.setEmail(EMAIL);
				 creator.setFullName(FULLNAME);
				 creator.setPassword(PASSWORD);
				 creator.setUsername(USERNAME);
				
				 for(int i = 1; i<5; i++) {
					 Ad ad = new Ad();
					 ad.setId(ID+i);
					 ad.setAddress(ADDRESS);
					 ad.setPickupDate(PICKUP_DATE);
					 ad.setPickupTimeStart(PICKUP_TIME_START);
					 ad.setPickupTimeEnd(PICKUP_TIME_END);
					 ad.setDropoffDate(DROPOFF_DATE);
					 ad.setDropoffTimeStart(DROPOFF_TIME_START);
					 ad.setDropoffTimeEnd(DROPOFF_TIME_END);
					 ad.setWeightKg(WEIGHT);
					 
					 int num = Integer.parseInt(ZIP.replace("A1A1A", ""))-i;
					 ad.setZipcode("A1A1A"+num);
					 
					 ad.setBleach(BLEACH);
					 ad.setIron(IRON);
					 ad.setFold(FOLD);
					 ad.setPhoneNumber(PHONE);
					 ad.setClothingDescription(DESCRIPTION);
					 if(isAvailable) ad.setLaundryStatus(LaundryStatus.POSTED);
					 else ad.setLaundryStatus(LaundryStatus.INPROGRESS);
					 ad.setCreator(creator);
					
					 list.add(ad);
				 }
			 }
			 
			 iterables = (Iterable<Ad>) list;
			 return iterables;
		 });
	}
	
	@Test
	public void testByAscPostalCodeNonEmpty() {
		isValid = true;
		isAvailable = true;
		List<AdDto> list = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeAsc();
		}catch(AdException e) {
			fail();
		}
		
		assertNotNull(list);
		for(int i=0; i<4; i++) {
			assertEquals(list.get(i).getAddress(), ADDRESS);
			assertEquals(list.get(i).getPickupDate(), PICKUP_DATE);
			assertEquals(list.get(i).getPickupTimeStart(), PICKUP_TIME_START);
			assertEquals(list.get(i).getPickupTimeEnd(), PICKUP_TIME_END);
			assertEquals(list.get(i).getDropoffDate(), DROPOFF_DATE);
			assertEquals(list.get(i).getDropoffTimeStart(), DROPOFF_TIME_START);
			assertEquals(list.get(i).getDropoffTimeEnd(), DROPOFF_TIME_END);
			assertEquals(list.get(i).getWeightKg(), WEIGHT);
			assertEquals(list.get(i).getBleach(), BLEACH);
			assertEquals(list.get(i).getIron(), IRON);
			assertEquals(list.get(i).getFold(), FOLD);
			assertEquals(list.get(i).getPhoneNumber(), PHONE);
			assertEquals(list.get(i).getClothingDescription(), DESCRIPTION);
			assertEquals(list.get(i).getLaundryStatus(), LaundryStatus.POSTED);
			assertEquals(list.get(i).getCreator().getUsername(), USERNAME);
			
			if(i<3) assertEquals(list.get(i).getZipcode().compareTo(list.get(i+1).getZipcode()) < 0, true);
			else  assertEquals(list.get(i).getZipcode(), "A1A1A9");
		}
		System.out.println(list);
	}
	
	@Test
	public void testByAscPostalCodeEmpty() {
		isValid = false;
		isAvailable = false;
		List<AdDto> list = null;
		String error = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeAsc();
		}catch(AdException e) {
			error = e.getMessage();
		}
		
		assertNotNull(error);
		assertNull(list);
		assertEquals("No advertisement exists", error);
	}
	
	@Test
	public void testByAscPostalCodeNonAvailable() {
		isValid = true;
		isAvailable = false;
		List<AdDto> list = null;
		String error = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeAsc();
		}catch(AdException e) {
			error = e.getMessage();
		}
		
		assertNotNull(error);
		assertNull(list);
		assertEquals("No available advertisement exists", error);
	}
	
	@Test
	public void testByDescPostalCodeNonEmpty() {
		isValid = true;
		isAvailable = true;
		List<AdDto> list = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeDesc();
		}catch(AdException e) {
			fail();
		}
		
		assertNotNull(list);
		for(int i=0; i<4; i++) {
			assertEquals(list.get(i).getAddress(), ADDRESS);
			assertEquals(list.get(i).getPickupDate(), PICKUP_DATE);
			assertEquals(list.get(i).getPickupTimeStart(), PICKUP_TIME_START);
			assertEquals(list.get(i).getPickupTimeEnd(), PICKUP_TIME_END);
			assertEquals(list.get(i).getDropoffDate(), DROPOFF_DATE);
			assertEquals(list.get(i).getDropoffTimeStart(), DROPOFF_TIME_START);
			assertEquals(list.get(i).getDropoffTimeEnd(), DROPOFF_TIME_END);
			assertEquals(list.get(i).getWeightKg(), WEIGHT);
			assertEquals(list.get(i).getBleach(), BLEACH);
			assertEquals(list.get(i).getIron(), IRON);
			assertEquals(list.get(i).getFold(), FOLD);
			assertEquals(list.get(i).getPhoneNumber(), PHONE);
			assertEquals(list.get(i).getClothingDescription(), DESCRIPTION);
			assertEquals(list.get(i).getLaundryStatus(), LaundryStatus.POSTED);
			assertEquals(list.get(i).getCreator().getUsername(), USERNAME);
			
			if(i<3) assertEquals(list.get(i).getZipcode().compareTo(list.get(i+1).getZipcode()) > 0, true);
			else  assertEquals(list.get(i).getZipcode(), "A1A1A1");
		}
		System.out.println(list);
	}
	
	@Test
	public void testByDescPostalCodeEmpty() {
		isValid = false;
		isAvailable = false;
		List<AdDto> list = null;
		String error = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeDesc();
		}catch(AdException e) {
			error = e.getMessage();
		}
		
		assertNotNull(error);
		assertNull(list);
		assertEquals("No advertisement exists", error);
	}
	
	@Test
	public void testByDescPostalCodeNonAvailable() {
		isValid = true;
		isAvailable = false;
		List<AdDto> list = null;
		String error = null;
		
		try {
			list = service.findAllAvailableAdsByZipcodeDesc();
		}catch(AdException e) {
			error = e.getMessage();
		}
		
		assertNotNull(error);
		assertNull(list);
		assertEquals("No available advertisement exists", error);
	}
	
}
