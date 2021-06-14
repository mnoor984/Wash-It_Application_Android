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
import washit.service.exception.AccountException;
import washit.service.exception.AdException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

class TestAdServiceAdsByAccount {

  private final AccountRepository accRepo = Mockito.mock(AccountRepository.class);
  private final AdRepository adRepo=Mockito.mock(AdRepository.class);

  private AdService adService;

  private static final String EXISTING_USERNAME="Im Here";
  private static final String NONEXIST_USERNAME="Im NotHere";

  @BeforeEach
  public void setMockOut() {
    adService= new AdServiceImpl(adRepo,accRepo);

    lenient().when(accRepo.existsByUsername(anyString())).thenAnswer((InvocationOnMock i)->{
      if (i.getArgument(0).equals(EXISTING_USERNAME)){
        return true;
      }
      else{
        return false;
      }
    });

  }

  @Test
  void testNonExistentAccount(){
    List<AdDto> dtos = null;
    String error = null;
    try {
      dtos = adService.findAllAdsForACustomer(NONEXIST_USERNAME);
    }
    catch (AccountException e){
      error=e.getMessage();
    }
    assertNull(dtos);
    assertEquals(error,"account does not exist");
  }

  @Test
  void testNoAdsInSystem(){

    when(adRepo.findAll()).thenReturn(new ArrayList<>());

    List<AdDto> dtos = null;
    String error = null;
    try {
      dtos = adService.findAllAdsForACustomer(EXISTING_USERNAME);
    }
    catch (AdException e){
      error=e.getMessage();
    }
    assertNull(dtos);
    assertEquals("Wash-It has no ads currently",error);
  }

  @Test
  void testNoAdsForAccount(){

    Account creator = new Account();
    creator.setUsername("creatorA");

    Ad ad1 = new Ad();
    ad1.setCreator(creator);

    Ad ad2 = new Ad();
    ad2.setCreator(creator);

    List<Ad> adList = new ArrayList<>();
    adList.add(ad1);
    adList.add(ad2);

    when(adRepo.findAll()).thenReturn(adList);

    List<AdDto> dtos = null;
    String error = null;
    try {
      dtos = adService.findAllAdsForACustomer(EXISTING_USERNAME);
    }
    catch (AdException e){
      error=e.getMessage();
    }
    assertNull(dtos);
    assertEquals("No ads posted by the specific customer",error);
  }

  @Test
  void testYesAdsForAccount(){

    Account creator = new Account();
    creator.setUsername(EXISTING_USERNAME);

    Ad ad1 = new Ad();
    ad1.setCreator(creator);

    Ad ad2 = new Ad();
    ad2.setCreator(creator);

    List<Ad> adList = new ArrayList<>();
    adList.add(ad1);
    adList.add(ad2);

    when(adRepo.findAll()).thenReturn(adList);

    List<AdDto> dtos = null;
    String error = null;
    try {
      dtos = adService.findAllAdsForACustomer(EXISTING_USERNAME);
    }
    catch (AdException e){
      fail();
    }
    assertNotNull(dtos);

    AdDto dto1 = dtos.get(0);
    AdDto dto2 = dtos.get(1);

    assertEquals(dto1.getCreator().getUsername(),EXISTING_USERNAME);
    assertEquals(dto2.getCreator().getUsername(),EXISTING_USERNAME);

  }
}





