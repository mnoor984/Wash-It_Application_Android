package washit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dto.AdDto;
import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.LaundryStatus;
import washit.service.exception.AccountException;
import washit.service.exception.AdException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

@Service
public class AdServiceImpl implements AdService {

  @Autowired
  private final AdRepository adRepo;

  @Autowired
  private final AccountRepository accRepo;

  public AdServiceImpl(AdRepository adRepo, AccountRepository accRepo) {
    this.adRepo = adRepo;
    this.accRepo=accRepo;
  }

  @Override
  @Transactional
  public AdDto updateAd(AdDto adDto, Integer adId) {

    Ad adEntity = adRepo.findAdById(adId)
                        .orElseThrow(() -> new AdException("Ad with id [" + adId + "] does not" + " exists"));


    checkMissingFields(adDto);
    validateDatesAndTimes(adDto);
    
    if (adDto.getPhoneNumber() == null || adDto.getPhoneNumber().equals(""))
        throw new AdException("Missing phone number");
      if (adDto.getClothingDescription() == null || adDto.getClothingDescription().equals(""))
        throw new AdException("Missing clothing description");
      if (adDto.getZipcode() == null || adDto.getZipcode().equals(""))
        throw new AdException("Missing zip code");
      if (adDto.getAddress() == null || adDto.getAddress().equals(""))
        throw new AdException("Missing address");
      if (adDto.getPickupDate() == null || adDto.getPickupTimeStart() == null || adDto.getPickupTimeEnd() == null)
        throw new AdException("Invalid pickup window");
      if (adDto.getDropoffDate() == null || adDto.getDropoffTimeStart() == null || adDto.getDropoffTimeEnd() == null)
        throw new AdException("Invalid drop-off window");

    adEntity.setAddress(adDto.getAddress());
    adEntity.setPickupDate(adDto.getPickupDate());
    adEntity.setPickupTimeStart(adDto.getPickupTimeStart());
    adEntity.setPickupTimeEnd(adDto.getPickupTimeEnd());
    adEntity.setDropoffDate(adDto.getDropoffDate());
    adEntity.setDropoffTimeStart(adDto.getDropoffTimeStart());
    adEntity.setDropoffTimeEnd(adDto.getDropoffTimeEnd());
    adEntity.setWeightKg(adDto.getWeightKg());
    adEntity.setZipcode(adDto.getZipcode());
    adEntity.setClothingDescription(adDto.getClothingDescription());
    adEntity.setSpecialInstructions(adDto.getSpecialInstructions());
    adEntity.setBleach(adDto.getBleach());
    adEntity.setIron(adDto.getIron());
    adEntity.setFold(adDto.getFold());
    adEntity.setPhoneNumber(adDto.getPhoneNumber());
    adEntity.setLaundryStatus(adDto.getLaundryStatus());

    Ad savedAdEntity = adRepo.save(adEntity);

    return AdDto.from(savedAdEntity);
  }

  @Override
  @Transactional
  public AdDto createAd(AdDto adDto) {
    // validation
    if (adDto.getCreator() == null || adDto.getCreator().getUsername() == null ||
        adDto.getCreator().getUsername().equals(""))
      throw new AdException("Missing creator username");
    if (adDto.getPhoneNumber() == null || adDto.getPhoneNumber().equals(""))
      throw new AdException("Missing phone number");
    if (adDto.getClothingDescription() == null || adDto.getClothingDescription().equals(""))
      throw new AdException("Missing clothing description");
    if (adDto.getZipcode() == null || adDto.getZipcode().equals(""))
      throw new AdException("Missing zip code");
    if (adDto.getAddress() == null || adDto.getAddress().equals(""))
      throw new AdException("Missing address");
    if (adDto.getPickupDate() == null || adDto.getPickupTimeStart() == null || adDto.getPickupTimeEnd() == null)
      throw new AdException("Invalid pickup window");
    if (adDto.getDropoffDate() == null || adDto.getDropoffTimeStart() == null || adDto.getDropoffTimeEnd() == null)
      throw new AdException("Invalid drop-off window");
    validateDatesAndTimes(adDto);
    // creation
    Ad adEntity = Ad.from(adDto);
    adEntity.setCreator(new Account(adDto.getCreator().getUsername(), null, null, null, null));
    Ad savedAdEntity = adRepo.save(adEntity);
    return AdDto.from(savedAdEntity);
  }

  @Override
  @Transactional
  public AdDto removeAd(Integer id) {

    if (id == null) {
      throw new AdException("Null Id. Imposible to remove Ad");
    }

    Ad ad = adRepo.findAdById(id).orElseThrow(() -> new AdException("No ad was found under the provided id"));

    LaundryStatus status = ad.getLaundryStatus();
    if (status != LaundryStatus.POSTED) {
      throw new AdException("Washing process is already in progress. Impossible to delete ad");
    }
    adRepo.delete(ad);

    return AdDto.from(ad);
  }

  @Override
  @Transactional
  public List<AdDto> removeAllAds() {

    List<AdDto> removedAds = new ArrayList<>();
    if (adRepo.count() == 0) {
        throw new AdException("No ads in system");
    }

    for(Ad a : adRepo.findAll()) {
    	removedAds.add(AdDto.from(a));
    	this.removeAd(a.getId());
    }

    return removedAds;
  }
  
  @Override
  @Transactional
  public AdDto findAd(int id) {
    return AdDto.from(adRepo.findAdById(id).orElseThrow(() -> new AdException("Ad does not exist")));
  }

  @Override
  @Transactional
  public List<AdDto> findAllAvailableAds() {

    if (toList(adRepo.findAll()).size() == 0) {
      throw new AdException("No advertisement exists");
    }

    List<Ad> adList = toList(adRepo.findAll());
    List<AdDto> availableAds = getAvailableAds(adList);
    
    if (availableAds.size() == 0) {
      throw new AdException("No available advertisement exists");
    }
    
    ArrayList<Integer> retrievedIds = new ArrayList<>();
    for(AdDto ad : availableAds) {
    	retrievedIds.add(ad.getId());
    }
    Collections.sort(retrievedIds);
    Collections.reverse(retrievedIds);
    
    ArrayList<AdDto> sortedAds = new ArrayList<>();
    boolean added = false;
    for(Integer i : retrievedIds) {
    	for(AdDto ad : availableAds ) {
    		if(ad.getId().equals(i) && added == false) {
    			sortedAds.add(ad);
    			added = true;
    		}
    	}
    	added = false;
    }
    return sortedAds;
  }

  @Override
  @Transactional
  public List<AdDto> findAllAvailableAdsByZipcodeAsc() {

    List<Ad> adList = adRepo.findAdsByOrderByZipcodeAsc();

    if (adList.size() == 0) {
      throw new AdException("No advertisement exists");
    }

    List<AdDto> availableAds = getAvailableAds(adList);

    if (availableAds.size() == 0) {
      throw new AdException("No available advertisement exists");
    }
    return availableAds;
  }

  @Override
  @Transactional
  public List<AdDto> findAllAvailableAdsByZipcodeDesc() {

    List<Ad> adList = adRepo.findAdsByOrderByZipcodeDesc();

    if (adList.size() == 0) {
      throw new AdException("No advertisement exists");
    }

    List<AdDto> availableAds = getAvailableAds(adList);

    if (availableAds.size() == 0) {
      throw new AdException("No available advertisement exists");
    }
    return availableAds;
  }

  // Helper method for getting an ArrayList of POSTED ads given a List of ads
  private ArrayList<AdDto> getAvailableAds(List<Ad> adList) {
    ArrayList<AdDto> availableAds = new ArrayList<>();

    for (Ad ad : adList) {
      if (ad.getLaundryStatus() == LaundryStatus.POSTED) {
        availableAds.add(AdDto.from(ad));
      }
    }

    return availableAds;
  }


  @Override
  @Transactional
  public List<AdDto> findAllAdsForACustomer(String customer_username) {
    List<AdDto> availableAds = new ArrayList<>();

    if (!accRepo.existsByUsername(customer_username)){
      throw new AccountException("account does not exist");
    }

    if (adRepo.findAll().isEmpty()) {
      throw new AdException("Wash-It has no ads currently");
    }

    List<Ad> adList = toList(adRepo.findAll());

    for (Ad ad : adList) {
      if (ad.getLaundryStatus() == LaundryStatus.POSTED && customer_username.equals(ad.getCreator().getUsername())) {
        availableAds.add(AdDto.from(ad));
      }
    }

    if (availableAds.size()==0) {
      throw new AdException("No ads posted by the specific customer");
    }
    return availableAds;

  }

  @Override
  @Transactional
  public boolean testMock(String username){
    return accRepo.existsByUsername(username);
  }



  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

  private void validateDatesAndTimes(AdDto adDto) {

    if (adDto.getPickupDate().isAfter(adDto.getDropoffDate())) {
      throw new AdException("pickup date cannot be after dropoff date");
    }

    if (adDto.getDropoffTimeStart().isAfter(adDto.getDropoffTimeEnd())) {
      throw new AdException("dropoff start time cannot be after dropoff end time");
    }

    if (adDto.getPickupTimeStart().isAfter(adDto.getPickupTimeEnd())) {
      throw new AdException("pickup start time cannot be after pickup end time");
    }

    LocalDateTime pickupEnd = LocalDateTime.of(adDto.getPickupDate(), adDto.getPickupTimeEnd());
    LocalDateTime dropoffStart = LocalDateTime.of(adDto.getDropoffDate(), adDto.getDropoffTimeStart());
    if (pickupEnd.until(dropoffStart, HOURS) < 4) {
      throw new AdException("Dropoff start time must be at least 4 hours after pickup end time.");
    }

    if (adDto.getPickupTimeStart().until(adDto.getPickupTimeEnd(), HOURS) < 1)
      throw new AdException("Pickup window must be at least 1 hour long");

    if (adDto.getDropoffTimeStart().until(adDto.getDropoffTimeEnd(), HOURS) < 1)
      throw new AdException("Drop-off window must be at least 1 hour long");
  }

  private void checkMissingFields(AdDto ad) {
    if (ad.getAddress() == null)
      throw new AdException("Missing Address");
    if (ad.getPickupDate() == null)
      throw new AdException("Missing pickup date");
    if (ad.getPickupTimeStart() == null)
      throw new AdException("Missing pickup time start");
    if (ad.getPickupTimeEnd() == null)
      throw new AdException("Missing pickup time end");
    if (ad.getDropoffDate() == null)
      throw new AdException("Missing dropoff date");
    if (ad.getDropoffTimeStart() == null)
      throw new AdException("Missing dropoff time start");
    if (ad.getDropoffTimeEnd() == null)
      throw new AdException("Missing dropoff time end");
    if (ad.getWeightKg() == null)
      throw new AdException("Missing weight");
    if (ad.getZipcode() == null)
      throw new AdException("Missing zipcode");
    if (ad.getClothingDescription() == null)
      throw new AdException("Missing clothing description");
    if (ad.getSpecialInstructions() == null)
      throw new AdException("Missing special instructions");
    if (ad.getBleach() == null)
      throw new AdException("Missing bleach info");
    if (ad.getIron() == null)
      throw new AdException("Missing iron info");
    if (ad.getFold() == null)
      throw new AdException("Missing fold info");
    if (ad.getPhoneNumber() == null)
      throw new AdException("Missing phone number");


  }

}
