package washit.service.Bid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.AccountDto;
import washit.dto.AdDto;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.service.BidService;
import washit.entity.LaundryStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestBidServiceCreate {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private AccountRepository accRepository;

    @InjectMocks
    private BidService bid_service;

    private Bid bid;
    private BidDto bid_Dto;
    private Account bid_account;
    private Ad createdAd;

    private static final Integer NONEXISTING_AD_ID=-1;
    private static final Integer AD_ID=1;
    private static final String AD_CREATOR_PASSWORD="passCreater";
    private static final String AD_CREATOR_USERNAME="Ad Creator";
    private static final String AD_CREATOR_FULLNAME="Newton";
    private static final String AD_CREATOR_EMAIL = "creator@gmail.com";

    private static final String BID_CREATOR_PASSWORD="passBidder";
    private static final String BID_CREATOR_USERNAME="Bidder";
    private static final String BID_CREATOR_FULLNAME="Albert Einstein";
    private static final String BID_CREATOR_EMAIL = "bidder@gmail.com";

    private static final Float BID_AMOUNT=100F;
    private static final LocalDateTime BID_TIME=LocalDateTime.of(2021,2,21,8,30,15);

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
        adDto.setLaundryStatus(LaundryStatus.POSTED);
        adDto.setCreator(user);

        Ad ad = Ad.from(adDto);
        return ad;
    }

    @BeforeEach
    public void setup(){

        lenient().when(adRepository.findAdById(anyInt())).thenAnswer(invocation ->{
            Integer id = invocation.getArgument(0);

            Optional<Ad> targetAd;

            if (id == NONEXISTING_AD_ID){
                targetAd=Optional.empty();
            }
            else {
                AccountDto user = new AccountDto(AD_CREATOR_USERNAME, AD_CREATOR_EMAIL, AD_CREATOR_FULLNAME, AD_CREATOR_PASSWORD);
                createdAd = makeAd(id, user);
                targetAd = Optional.of(createdAd);
            }
            return targetAd;
        });

        lenient().when(accRepository.findByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
            bid_account = new Account();
            bid_account.setUsername(BID_CREATOR_USERNAME);
            bid_account.setFullName(BID_CREATOR_FULLNAME);
            bid_account.setEmail(BID_CREATOR_EMAIL);
            bid_account.setPassword(BID_CREATOR_PASSWORD);
            return bid_account;
        });

    } //end of setup


    @Test
    public void bid_create_success() {

        bid = new Bid();
        bid.setAdId(createdAd);
        bid.setCreator(bid_account);
        bid.setBidAmount(BID_AMOUNT);
        bid.setDateTimeCreated(BID_TIME);
        bid.setIsAccepted(false);

        bid_Dto = new BidDto();
        bid_Dto.setUsername(BID_CREATOR_USERNAME);
        bid_Dto.setAdId(AD_ID);
        bid_Dto.setAmount(BID_AMOUNT);
        bid_Dto.setDateTimeCreated(BID_TIME);
        bid.setIsAccepted(false);

        when(bidRepository.save(any(Bid.class))).thenReturn(bid);
        Bid bid_create = bid_service.createBid(bid_Dto);
        assertEquals(bid_create, bid);
    }

    @Test
    public void bid_on_nonexisting_ad() {

        bid_Dto = new BidDto();
        bid_Dto.setUsername(BID_CREATOR_USERNAME);
        bid_Dto.setAdId(NONEXISTING_AD_ID);
        bid_Dto.setAmount(BID_AMOUNT);
        bid_Dto.setIsAccepted(false);

        String error=null;
        try{
            Bid bid_create = bid_service.createBid(bid_Dto);
        }
        catch (Exception e) {
            error=e.getMessage();
        }
        assertEquals("Ad not found", error);
    }

    @Test
    public void bid_on_own_ad() {

        bid_Dto = new BidDto();
        bid_Dto.setUsername(AD_CREATOR_USERNAME);
        bid_Dto.setAdId(AD_ID);
        bid_Dto.setAmount(BID_AMOUNT);
        bid_Dto.setDateTimeCreated(BID_TIME);
        bid_Dto.setIsAccepted(false);

        String error=null;
        try{
            Bid bid_create = bid_service.createBid(bid_Dto);
        }
        catch (Exception e) {
            error=e.getMessage();
        }
        assertEquals("You cannot bid on an ad you posted", error);
    }

} // end of class
