package washit.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dao.BidRepository;
import washit.dto.AdDto;
import washit.dto.BidDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.entity.LaundryStatus;
import washit.service.exception.AccountException;
import washit.service.exception.AdException;
import washit.service.exception.BidException;

@Service
public class BidService {

	@Autowired
	AccountRepository accRepo;

	@Autowired
	AdRepository adRepo;

	@Autowired
	BidRepository bidRepo;


	@Transactional
	public String find_Ad_username(int id) {
		AdDto ad_Dto = AdDto.from(adRepo.findAdById(id).orElseThrow(() -> new AdException("Ad does not exist")));
		Ad ad = Ad.from(ad_Dto);
		return ad.getCreator().getUsername();
	}

	@Transactional
	public List<BidDto> getAllBids(Integer adId) throws BidException {
		
		if(!adRepo.existsById(adId)) {
			throw new BidException("No ad with that id");
		}
		Optional<Ad> testAd = adRepo.findAdById(adId);

		if (testAd.isEmpty()){
			throw new BidException("No ad with that id");
		}

		List<Bid> testArray = bidRepo.findByAdId(testAd.get());
		if(testArray.size() == 0) {
			throw new BidException("there are currently no bids on this ad");

		}
		List<BidDto> bidDtos = new ArrayList<>();
		
		
		for(Bid b : testArray) {
			bidDtos.add(BidDto.from(b));
		}
		return bidDtos;

	}
	
	@Transactional
	public BidDto acceptBid(Integer adId,String username) throws BidException{ //WIP
		
		List<BidDto> bids = getAllBids(adId);
		
		for(BidDto bid : bids) {
			
			if(bid.getIsAccepted()) throw new BidException("Ad already has accepted bid");
		}
		
		Optional<Ad> ad = adRepo.findAdById(adId);

		ad.get().setLaundryStatus(LaundryStatus.INPROGRESS);

		Optional<Bid> bid = bidRepo.findByAdIdAndCreatorUsername(ad.get(), username);
		
	    bid.get().setIsAccepted(true);
	    Bid resultBid = bidRepo.save(bid.get());
	    return BidDto.from(resultBid);
		
	}
	

	@Transactional
	public Bid createBid(BidDto dto) throws BidException {

		String bidCreatorUsername = dto.getUsername();
		Integer adId = dto.getAdId();
		Float amount = dto.getAmount();
		LocalDateTime dateTimeCreated = dto.getDateTimeCreated();
		Boolean isAccepted = dto.getIsAccepted();

		// Get the ad being bid on
		Optional<Ad> adOptional = adRepo.findAdById(adId);

		if (adOptional.isEmpty()) {
			throw new BidException("Ad not found");
		}

		Ad ad = adOptional.get();

		// Check if the bidder is the same user who created the ad
		String adCreatorUsername = ad.getCreator().getUsername();
		if (bidCreatorUsername.equals(adCreatorUsername)) {
			throw new BidException("You cannot bid on an ad you posted");
		}

		Account creatorAcc = accRepo.findByUsername(bidCreatorUsername);

		// Create the bid
		Bid bid = new Bid();
		bid.setAdId(ad);
		bid.setCreator(creatorAcc);
		bid.setBidAmount(amount);
		bid.setDateTimeCreated(dateTimeCreated);
		bid.setIsAccepted(isAccepted);

		// Save the bid to the database
		bid = bidRepo.save(bid);
		return bid;
	}

	private <T> List<T> toList(Iterable<T> iterable) {
		List<T> resultList = new ArrayList<T>();
		for (T t : iterable) {
			resultList.add(t);
		}
		return resultList;
	}
}
