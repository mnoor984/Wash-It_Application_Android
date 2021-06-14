package washit.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import washit.controller.representation.AdRep;
import washit.dto.AdDto;
import washit.service.AdService;
import washit.service.RequestAuthenticator;
import washit.service.exception.AdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController()
public class AdRestController {

  private final AdService adService;
  private final RequestAuthenticator authenticator;

  public AdRestController(AdService service, RequestAuthenticator requestAuthenticator) {
    this.adService = service;
    this.authenticator = requestAuthenticator;
  }

  @PutMapping(value = {"/ads/{id}"})
  public ResponseEntity<AdRep> updateAd(@PathVariable("id") Integer id, @RequestBody AdRep newAdRep,
                                        @RequestHeader Map<String, String> headers) {
    // authorization
    String username = authenticator.getUsernameFromHeaders(headers);
    if (!newAdRep.getAccount().equals(username))
      throw new AdException("Cannot update ads for an account you are not signed into");

    AdDto adDto = AdRep.toAdDto(newAdRep);
    AdDto adDtoUpdated = adService.updateAd(adDto, id);
    AdRep adRep = AdRep.fromAdDto(adDtoUpdated);
    return new ResponseEntity<>(adRep, HttpStatus.OK);
  }

  @GetMapping("/ads/{id}")
  public AdRep getAd(@PathVariable("id") int id) {
    AdDto adDto = adService.findAd(id);
    return AdRep.fromAdDto(adDto);
  }

  @GetMapping("/ads")
  public List<AdRep> getAllAvailableAds() throws AdException {
    List<AdRep> adDtos = new ArrayList<>();
    for (AdDto adDto : adService.findAllAvailableAds()) {
      adDtos.add(AdRep.fromAdDto(adDto));
    }
    return adDtos;
  }

  @GetMapping("/ads/asc")
  public List<AdRep> getAllAvailableAdsByZipcodeAsc() throws AdException {
    List<AdRep> adDtos = new ArrayList<>();
    for (AdDto adDto : adService.findAllAvailableAdsByZipcodeAsc()) {
      adDtos.add(AdRep.fromAdDto(adDto));
    }
    return adDtos;
  }

  @GetMapping("/ads/desc")
  public List<AdRep> getAllAvailableAdsByZipcodeDesc() throws AdException {
    List<AdRep> adDtos = new ArrayList<>();
    for (AdDto adDto : adService.findAllAvailableAdsByZipcodeDesc()) {
      adDtos.add(AdRep.fromAdDto(adDto));
    }
    return adDtos;
  }

  @GetMapping("/ads/account/{customer_username}")
  public List<AdRep> getAllAdsForCustomer(@PathVariable("customer_username") String customer_username) throws AdException {
    List<AdRep> adDtos = new ArrayList<>();
    for (AdDto adDto : adService.findAllAdsForACustomer(customer_username)) {
      adDtos.add(AdRep.fromAdDto(adDto));
    }
    return adDtos;
  }

  @PostMapping("/ads")
  public AdRep createAd(@RequestBody AdRep newAdRep, @RequestHeader Map<String, String> headers) {
    // authorization
    String username = authenticator.getUsernameFromHeaders(headers);
    if (!newAdRep.getAccount().equals(username))
      throw new AdException("Cannot create ads for an account you are not signed into");

    AdDto adDto = AdRep.toAdDto(newAdRep);
    AdDto createdAd = adService.createAd(adDto);
    return AdRep.fromAdDto(createdAd);
  }

  @GetMapping("/hi")
  public String test() {
    return "hey!";
  }

  @DeleteMapping(value = {"/ads/{id}", "/ads/{id}/"})
  public ResponseEntity<String> removeAd(@PathVariable("id") Integer id, @RequestHeader Map<String, String> headers) {
    AdDto adToDelete = adService.findAd(id);
    // authorization
    String username = authenticator.getUsernameFromHeaders(headers);
    if (!adToDelete.getCreator().getUsername().equals(username))
      throw new AdException("Cannot delete ads for an account you are not signed into");

    adService.removeAd(id);
    return new ResponseEntity<>("Ok", HttpStatus.OK);
  }
}
