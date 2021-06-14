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
import washit.service.AdService;
import washit.service.AdServiceImpl;
import washit.service.exception.AdException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


public class TestAdServiceUpdate {

	
	private final AdRepository adRepo = Mockito.mock(AdRepository.class);
	private final AccountRepository accRepo = Mockito.mock(AccountRepository.class);

	private AdService adService;
	
	private static final int EXISTING_ID = 2;
	private static final int INVALID_ID = 3;
	private static final String VALID_ADDRESS = "123 boulvard";
	private static final String VALID_INSTRUCTIONS = "do not machine dry jeans";
	private static final LocalDate VALID_PICKUP_DATE = LocalDate.of(2021, 3, 18);
	private static final LocalDate INVALID_PICKUP_DATE = LocalDate.of(2021, 3, 21);
	private static final LocalTime VALID_PICKUP_TIME_START = LocalTime.of(8, 45);
	private static final LocalTime INVALID_PICKUP_TIME_START = LocalTime.of(10, 45);
	private static final LocalTime VALID_PICKUP_TIME_END = LocalTime.of(9, 45);
	private static final LocalDate VALID_DROPOFF_DATE = LocalDate.of(2021, 3, 20);
	private static final LocalTime VALID_DROPOFF_TIME_START = LocalTime.of(7, 30);
	private static final LocalTime VALID_DROPOFF_TIME_END = LocalTime.of(8, 30);
	private static final float VALID_WEIGHT = 2.5f;
	private static final String VALID_ZIP = "D1E2F3";
	private static final Boolean VALID_BLEACH = true;
	private static final Boolean VALID_IRON = false;
	private static final Boolean VALID_FOLD = false;
	private static final String VALID_PHONE = "0987654321";
	private static final String VALID_DESCRIPTION = "super swag";
	
	
	
	@BeforeEach
	public void setMockOut() {
		
		adService = new AdServiceImpl(adRepo,accRepo);

		when(adRepo.existsById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(EXISTING_ID)) {
				return true;
			} else {
				return false;
			}
		});

		Optional<Ad> ad = Optional.of(new Ad());
		Account user = new Account("mike13", "mike13@gmail.com", "Mike", "aStrongPassword", true);

		ad.get().setCreator(user);
		ad.get().setAddress("224 somewhere");
		ad.get().setPickupDate(LocalDate.of(2021, 3, 19));
		ad.get().setPickupTimeStart(LocalTime.of(9, 30));
		ad.get().setPickupTimeEnd(LocalTime.of(10, 00));
		ad.get().setDropoffDate(LocalDate.of(2021, 3, 25));
		ad.get().setDropoffTimeStart(LocalTime.of(17, 30));
		ad.get().setDropoffTimeEnd(LocalTime.of(18, 30));
		ad.get().setWeightKg(1.0f);
	    ad.get().setZipcode("A1B2C3");
	    ad.get().setClothingDescription("2 T-shirts, 4 pants");
	    ad.get().setSpecialInstructions("");
	    ad.get().setBleach(false);
	    ad.get().setIron(true);
	    ad.get().setFold(true);
	    ad.get().setPhoneNumber("1234567890");
	    
	   when(adRepo.findAdById(EXISTING_ID)).thenReturn(ad); 
	   when(adRepo.save(any())).thenReturn(ad.get()); 
	   when(adRepo.findAdById(INVALID_ID)).thenThrow(new AdException("Ad with id [" + INVALID_ID + "] does not" + " exists")); 


		
	}
	
	
	
	
	@Test
	public void testUpdateAdAllValid() {
		AdDto adDto = new AdDto();
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
			result = adRepo.findAdById(EXISTING_ID); 
		}
		catch(AdException e) {
		
			fail();
		}

		assertNotNull(result);
		assertEquals(result.get().getAddress(), VALID_ADDRESS);
		assertEquals(result.get().getPhoneNumber(), VALID_PHONE);
		assertEquals(result.get().getClothingDescription(), VALID_DESCRIPTION);
		assertEquals(result.get().getDropoffDate(), VALID_DROPOFF_DATE);
		assertEquals(result.get().getPickupDate(), VALID_PICKUP_DATE);
		assertEquals(result.get().getDropoffTimeStart(), VALID_DROPOFF_TIME_START);
		assertEquals(result.get().getDropoffTimeEnd(), VALID_DROPOFF_TIME_END);
		assertEquals(result.get().getPickupTimeStart(), VALID_PICKUP_TIME_START);
		assertEquals(result.get().getPickupTimeEnd(), VALID_PICKUP_TIME_END);
		assertEquals(result.get().getIron(), VALID_IRON);
		assertEquals(result.get().getBleach(), VALID_BLEACH);
		assertEquals(result.get().getFold(), VALID_FOLD);
		assertEquals(result.get().getWeightKg(), VALID_WEIGHT);
		assertEquals(result.get().getZipcode(), VALID_ZIP);
		assertEquals(result.get().getSpecialInstructions(), VALID_INSTRUCTIONS);

	}
	
	@Test
	public void invalidId() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		

		try {
			adService.updateAd(adDto, INVALID_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Ad with id [3] does not exists");

	}
	
	@Test
	public void pickupDateBeforeDropoff() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(INVALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "pickup date cannot be after dropoff date");

	}
	
	@Test
	public void pickupTimeStartBeforeEnd() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(INVALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "pickup start time cannot be after pickup end time");

	}
	
	@Test
	public void addressEmpty() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress("");
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing address");

	}
	
	@Test
	public void missingAddress() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(null);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing Address");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getAddress(), "224 somewhere"); 

	}
	
	@Test
	public void missingPickupDate() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(null);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing pickup date");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getPickupDate(), LocalDate.of(2021, 3, 19)); 

	}
	
	@Test
	public void missingDropOffDate() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(null);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing dropoff date");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getDropoffDate(), LocalDate.of(2021, 3, 25)); 

	}
	
	@Test
	public void missingPickupTimeStart() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(null);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing pickup time start");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getPickupTimeStart(), LocalTime.of(9, 30));

	}
	
	@Test
	public void missingPickupTimeEnd() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(null);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing pickup time end");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getPickupTimeEnd(), LocalTime.of(10, 00)); 

	}
	
	@Test
	public void missingDropOffTimeStart() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(null);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD); 
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing dropoff time start");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getDropoffTimeStart(), LocalTime.of(17, 30)); 

	}

	@Test
	public void missingDropOffTimeEnd() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(null);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing dropoff time end");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getDropoffTimeEnd(), LocalTime.of(18, 30)); 

	}
	
	@Test
	public void missingPhone() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(null);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing phone number");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getPhoneNumber(), "1234567890"); 

	}
	
	@Test
	public void missingInstructions() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(null);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing special instructions");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getSpecialInstructions(), ""); 

	}
	
	@Test
	public void missingDescription() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(null);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing clothing description");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getClothingDescription(), "2 T-shirts, 4 pants"); 

	}
	
	@Test
	public void missingBleach() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(null);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing bleach info");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getBleach(), false); 

	}
	
	@Test
	public void missingIron() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(null);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing iron info");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getIron(), true); 

	}
	
	@Test
	public void missingFold() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(null);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing fold info");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getFold(), true); 

	}
	
	@Test
	public void missingWeight() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(null);
		adDto.setZipcode(VALID_ZIP);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing weight");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getWeightKg(), 1.0f); 

	}
	
	@Test
	public void missingZipcode() {
		AdDto adDto = new AdDto();
		String error= "";
		
		adDto.setAddress(VALID_ADDRESS);
		adDto.setPickupDate(VALID_PICKUP_DATE);
		adDto.setPhoneNumber(VALID_PHONE);
		adDto.setDropoffDate(VALID_DROPOFF_DATE);
		adDto.setPickupTimeStart(VALID_PICKUP_TIME_START);
		adDto.setPickupTimeEnd(VALID_PICKUP_TIME_END);
		adDto.setDropoffTimeStart(VALID_DROPOFF_TIME_START);
		adDto.setDropoffTimeEnd(VALID_DROPOFF_TIME_END);
		adDto.setClothingDescription(VALID_DESCRIPTION);
		adDto.setBleach(VALID_BLEACH);
		adDto.setFold(VALID_FOLD);
		adDto.setIron(VALID_IRON);
		adDto.setWeightKg(VALID_WEIGHT);
		adDto.setZipcode(null);
		adDto.setSpecialInstructions(VALID_INSTRUCTIONS);
		
		Optional<Ad> result = null;

		try {
			adService.updateAd(adDto, EXISTING_ID);
		}
		catch(AdException e) {
			error = e.getMessage();
		}

		assertEquals(error, "Missing zipcode");
		result = adRepo.findAdById(EXISTING_ID);
		assertEquals(result.get().getWeightKg(), 1.0f); 

	}
}
