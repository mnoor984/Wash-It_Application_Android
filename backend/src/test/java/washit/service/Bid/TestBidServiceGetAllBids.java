package washit.service.Bid;

import io.cucumber.java.bs.A;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.AccountDto;
import washit.dto.AdDto;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.service.AccountService;
import washit.service.BidService;
import washit.service.exception.AccountException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestBidServiceGetAllBids {

	@Mock
	private BidRepository bidRepository;

	@Mock
	private AdRepository adRepository;

	@InjectMocks
	private BidService service;


	private static final Integer AD_WITH_BIDS_ID=283;
	private static final Integer AD_NO_BIDS_ID=859;
	private static final Integer NONEXIST_AD_ID=-1;

	private static final String PASSWORD="Pass123!";

	private static final String CREATOR_USERNAME="Ad Creator";
	private static final String CREATOR_FULLNAME="Bobby Brown";

	private static final String BIDDER_A_USERNAME="Bidder A";
	private static final String BIDDER_A_FULLNAME="A A";
	private static final Float BID_A_AMOUNT=100F;
	private static final LocalDateTime BID_A_TIME=LocalDateTime.of(2021,2,21,8,30,15);

	private static final String BIDDER_B_USERNAME="Bidder B";
	private static final String BIDDER_B_FULLNAME="B B";
	private static final Float BID_B_AMOUNT=50F;
	private static final LocalDateTime BID_B_TIME=LocalDateTime.of(2021,2,22,18,43,37);


	private Ad makeAd(Integer id, AccountDto user){
		AdDto adDto = new AdDto();

		adDto.setAddress("123 some place");
		adDto.setPickupDate(LocalDate.of(2021, 3, 12));
		adDto.setPickupTimeStart(LocalTime.of(10, 30));
		adDto.setPickupTimeEnd(LocalTime.of(12, 30));
		adDto.setDropoffDate(LocalDate.of(2021, 3, 12));
		adDto.setDropoffTimeStart(LocalTime.of(17, 30));
		adDto.setDropoffTimeEnd(LocalTime.of(18, 30));
		adDto.setWeightKg(1.0f);
		adDto.setZipcode("A1B2C3");
		adDto.setClothingDescription("2 T-shirts, 4 pants");
		adDto.setSpecialInstructions("");
		adDto.setBleach(false);
		adDto.setIron(true);
		adDto.setFold(true);
		adDto.setPhoneNumber("1234567890");

		adDto.setCreator(user);

		Ad ad = Ad.from(adDto);
		ad.setId(id);

		return ad;
	}

	@BeforeEach
	public void setup(){

		lenient().when(adRepository.existsById(anyInt())).thenAnswer((InvocationOnMock invocation) -> {
			if (invocation.getArgument(0).equals(NONEXIST_AD_ID)){
				return false;
			}
			else{
				return true;
			}
		});

		lenient().when(adRepository.findAdById(anyInt())).thenAnswer(invocation ->{
			Integer id = invocation.getArgument(0);

			Optional<Ad> targetAd;

			if (id == NONEXIST_AD_ID){
				targetAd=Optional.empty();
			}
			else{
				AccountDto user = new AccountDto(CREATOR_USERNAME, "creator@gmail.com", CREATOR_FULLNAME, PASSWORD);
				Ad createdAd = makeAd(id,user);
				createdAd.setId(id);
				targetAd = Optional.of(createdAd);
			}
			return targetAd;
		});

		lenient().when(bidRepository.findByAdId(any())).thenAnswer(invocation -> {
			Ad adOfBid = invocation.getArgument(0);
			Integer id = adOfBid.getId();

			if (id != AD_WITH_BIDS_ID){
				return new ArrayList<Bid>();
			}
			else{

				Account bidderA = new Account(BIDDER_A_USERNAME, "a@gmail.com", BIDDER_A_FULLNAME, PASSWORD, false);
				Account bidderB = new Account(BIDDER_B_USERNAME, "b@gmail.com", BIDDER_B_FULLNAME, PASSWORD, false);


				Bid bidA = new Bid();
				bidA.setAdId(adOfBid);
				bidA.setCreator(bidderA);
				bidA.setBidAmount(BID_A_AMOUNT);
				bidA.setDateTimeCreated(BID_A_TIME);
				bidA.setIsAccepted(false);


				Bid bidB = new Bid();
				bidB.setAdId(adOfBid);
				bidB.setCreator(bidderB);
				bidB.setBidAmount(BID_B_AMOUNT);
				bidB.setDateTimeCreated(BID_B_TIME);
				bidB.setIsAccepted(false);


				List<Bid> bidList = new ArrayList<>();
				bidList.add(bidA);
				bidList.add(bidB);

				return bidList;
			}
		});

	}

	@Test
	public void testExistingAdWithBids() {
		List<BidDto> bidDtos=null;

		try{
			bidDtos = service.getAllBids(AD_WITH_BIDS_ID);
		}
		catch (Exception e){
			System.out.println(e.getMessage());
			fail();
		}
		assertNotNull(bidDtos);
		assertEquals(bidDtos.size(),2);

		BidDto bidA=bidDtos.get(0);
		BidDto bidB=bidDtos.get(1);

		assertEquals(bidA.getAdId(),AD_WITH_BIDS_ID);
		assertEquals(bidA.getAmount(),BID_A_AMOUNT);
		assertEquals(bidA.getUsername(), BIDDER_A_USERNAME);
		assertEquals(bidA.getDateTimeCreated(), BID_A_TIME);
		assertEquals(bidA.getIsAccepted(), false);

		assertEquals(bidB.getAdId(),AD_WITH_BIDS_ID);
		assertEquals(bidB.getAmount(),BID_B_AMOUNT);
		assertEquals(bidB.getUsername(), BIDDER_B_USERNAME);
		assertEquals(bidB.getDateTimeCreated(), BID_B_TIME);
		assertEquals(bidB.getIsAccepted(), false);
	}

	@Test
	public void testExistingAdWithNoBids(){
		List<BidDto> bidDtos=null;
		String error=null;
		try{
			bidDtos = service.getAllBids(AD_NO_BIDS_ID);
		}
		catch (Exception e) {
			error=e.getMessage();
		}
		assertNull(bidDtos);
		assertEquals(error,"there are currently no bids on this ad");
	}

	@Test
	public void testNonExistentAd(){
		List<BidDto> bidDtos=null;
		String error=null;
		try{
			bidDtos = service.getAllBids(NONEXIST_AD_ID);
		}
		catch (Exception e) {
			error=e.getMessage();
		}
		assertNull(bidDtos);
		assertEquals(error,"No ad with that id");
	}
}
