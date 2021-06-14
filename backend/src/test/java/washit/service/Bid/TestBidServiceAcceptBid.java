package washit.service.Bid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestBidServiceAcceptBid {

    @Mock
    private BidRepository bidRepository;

    @Mock
    private AdRepository adRepository;

    @Mock
    private AccountRepository accRepository;

    @InjectMocks
    private BidService bidService;

    private Bid createdBid;
    private Ad createdAd;
    private Ad adWithAcceptedBid;
    private Ad adWithNoBids;

    private static final Integer AD_ID=1;
    private static final Integer AD_ID_ACCEPTED_BID = 2;
    private static final Integer AD_ID_NO_BIDS = 3;
    private static final String AD_CREATOR_PASSWORD="passCreater";
    private static final String AD_CREATOR_USERNAME="AdCreator";
    private static final String AD_CREATOR_FULLNAME="Newton";
    private static final String AD_CREATOR_EMAIL = "creator@gmail.com";

    private static final String BID_CREATOR_PASSWORD="passBidder";
    private static final String BID_CREATOR_USERNAME="Bidder";
    private static final String BID_CREATOR_FULLNAME="Bid Der";
    private static final String BID_CREATOR_EMAIL = "bidder@gmail.com";

    private static final Float BID_AMOUNT=100F;
    private static final LocalDateTime BID_TIME=LocalDateTime.of(2021,3,26,8,30,15);


    private Ad mockAd(Integer id, AccountDto user){
        AdDto adDto = new AdDto();

        adDto.setAddress("123 some place");
        adDto.setPickupDate(LocalDate.of(2021, 3, 28));
        adDto.setPickupTimeStart(LocalTime.of(10, 30));
        adDto.setPickupTimeEnd(LocalTime.of(12, 30));
        adDto.setDropoffDate(LocalDate.of(2021, 3, 28));
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
        lenient().when(adRepository.existsById(anyInt())).thenAnswer(invocation -> true);

        lenient().when(adRepository.findAdById(anyInt())).thenAnswer(invocation ->{
            Integer adId = invocation.getArgument(0);

            AccountDto user = new AccountDto(AD_CREATOR_USERNAME, AD_CREATOR_EMAIL, AD_CREATOR_FULLNAME, AD_CREATOR_PASSWORD);

            if (adId == AD_ID) {
                createdAd = mockAd(adId, user);
                return Optional.of(createdAd);
            } else if (adId == AD_ID_ACCEPTED_BID){
                adWithAcceptedBid = mockAd(adId, user);
                return Optional.of(adWithAcceptedBid);
            } else {
                adWithNoBids = mockAd(adId, user);
                return Optional.of(adWithNoBids);
            }
        });

        lenient().when(bidRepository.findByAdId(any())).thenAnswer(invocation -> {
            Ad ad = invocation.getArgument(0);

            List<Bid> bids = new ArrayList<>();

            Account bidCreator = new Account(BID_CREATOR_USERNAME, BID_CREATOR_EMAIL, BID_CREATOR_FULLNAME, BID_CREATOR_PASSWORD, false);

            if (ad.equals(createdAd)) {
                createdBid = new Bid(createdAd, bidCreator, BID_AMOUNT, BID_TIME, false);
                bids.add(createdBid);
            } else if (ad.equals(adWithAcceptedBid)) {
                createdBid = new Bid(adWithAcceptedBid, bidCreator, BID_AMOUNT, BID_TIME, true);
                bids.add(createdBid);
            }

            return bids;
        });

        lenient().when(bidRepository.findByAdIdAndCreatorUsername(any(), anyString())).thenAnswer(invocation ->{
            Optional<Bid> optionalBid;

            optionalBid = Optional.of(createdBid);

            return optionalBid;
        });

        lenient().when(bidRepository.save(Mockito.any(Bid.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));

    }

    @Test
    public void testAcceptBidSuccess() {
        BidDto acceptedBid = bidService.acceptBid(AD_ID, BID_CREATOR_USERNAME);

        assertEquals(true, acceptedBid.getIsAccepted());
        assertEquals(BID_CREATOR_USERNAME, acceptedBid.getUsername());
    }

    @Test
    public void testAcceptBidFailAlreadyAcceptedBid() {
        BidDto bid = null;
        String error = null;

        try {
            bid = bidService.acceptBid(AD_ID_ACCEPTED_BID, BID_CREATOR_USERNAME);
        }
        catch(Exception e) {
            error = e.getMessage();
        }

        assertNull(bid);
        assertEquals(error,"Ad already has accepted bid");
    }

    @Test
    public void testAcceptBidFailNoBidsOnAd() {
        BidDto bid = null;
        String error = null;

        try {
            bid = bidService.acceptBid(AD_ID_NO_BIDS, BID_CREATOR_USERNAME);
        }
        catch(Exception e) {
            error = e.getMessage();
        }

        assertNull(bid);
        assertEquals(error,"there are currently no bids on this ad");
    }

}
