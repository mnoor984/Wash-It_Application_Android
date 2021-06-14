package washit.service;

import washit.dto.AdDto;

import java.util.List;

public interface AdService {

  AdDto createAd(AdDto adDto);

  AdDto updateAd(AdDto adDto, Integer adId);

  List<AdDto> findAllAvailableAds();

  List<AdDto> findAllAvailableAdsByZipcodeAsc();

  List<AdDto> findAllAvailableAdsByZipcodeDesc();

  List<AdDto> findAllAdsForACustomer(String customer_username);

  AdDto removeAd(Integer id);

  AdDto findAd(int id);

  List<AdDto> removeAllAds();

  boolean testMock(String username);
}
