package washit.service.Ad;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dto.AccountDto;
import washit.dto.AdDto;
import washit.entity.Ad;
import washit.entity.LaundryStatus;
import washit.service.AdService;
import washit.service.AdServiceImpl;
import washit.service.exception.AdException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class TestAdServiceCreate {
  private final AdRepository adRepo = Mockito.mock(AdRepository.class);
  private final AccountRepository accRepo = Mockito.mock(AccountRepository.class);

  private AdService adService;
  private AdDto ad;
  private AdDto minimumInfoAd;

  @BeforeEach
  void initAdService() {
    adService = new AdServiceImpl(adRepo,accRepo);
    AccountDto user = new AccountDto("user123", "a@gmail.com", "Bob", "aStrongPassword");
    ad = new AdDto();
    ad.setAddress("123 some place");
    ad.setPickupDate(LocalDate.of(2021, 3, 12));
    ad.setPickupTimeStart(LocalTime.of(10, 30));
    ad.setPickupTimeEnd(LocalTime.of(12, 30));
    ad.setDropoffDate(LocalDate.of(2021, 3, 12));
    ad.setDropoffTimeStart(LocalTime.of(17, 30));
    ad.setDropoffTimeEnd(LocalTime.of(18, 30));
    ad.setWeightKg(1.0f);
    ad.setZipcode("A1B2C3");
    ad.setClothingDescription("2 T-shirts, 4 pants");
    ad.setSpecialInstructions("");
    ad.setBleach(false);
    ad.setIron(true);
    ad.setFold(true);
    ad.setPhoneNumber("1234567890");
    ad.setCreator(user);

    this.minimumInfoAd = new AdDto();
    minimumInfoAd.setPhoneNumber("123-456-7890");
    minimumInfoAd.setClothingDescription("clothing description");
    minimumInfoAd.setAddress("123 some street");
    minimumInfoAd.setZipcode("1A2B3C");
    var pickupStart = LocalDateTime.of(2021, 1, 1, 8, 0);
    var pickupEnd = LocalDateTime.of(2021, 1, 1, 9, 0);
    var dropoffStart = LocalDateTime.of(2021, 1, 1, 17, 0);
    var dropoffEnd = LocalDateTime.of(2021, 1, 1, 18, 0);

    minimumInfoAd.setPickupDate(pickupStart.toLocalDate());
    minimumInfoAd.setPickupTimeStart(pickupStart.toLocalTime());
    minimumInfoAd.setPickupTimeEnd(pickupEnd.toLocalTime());

    minimumInfoAd.setDropoffDate(dropoffStart.toLocalDate());
    minimumInfoAd.setDropoffTimeStart(dropoffStart.toLocalTime());
    minimumInfoAd.setDropoffTimeEnd(dropoffEnd.toLocalTime());

    minimumInfoAd.setCreator(new AccountDto("user", "email@asdf.ca", "full name", "pass"));
  }

  @Test
  void windowAtLeast1HourWide() {
    ad.setPickupTimeStart(LocalTime.of(9, 0));
    ad.setPickupTimeEnd(LocalTime.of(9, 59, 59));
    ad.setDropoffTimeStart(LocalTime.of(17, 0));
    ad.setDropoffTimeEnd(LocalTime.of(17, 59, 59));

    assertThrows(AdException.class, () -> adService.createAd(ad));
  }

  @Test
  void allValidData() {
    AdDto result = ad.copy();
    result.setId(1);
    result.setLaundryStatus(LaundryStatus.POSTED);

    when(adRepo.save(any(Ad.class))).thenReturn(Ad.from(result));
    AdDto adResult = adService.createAd(ad);

    assertEquals(result, adResult);
  }

  @Test
  void minimumFields() {
    AdDto result = minimumInfoAd.copy();
    result.setId(1);
    result.setLaundryStatus(LaundryStatus.POSTED);

    when(adRepo.save(any(Ad.class))).thenReturn(Ad.from(result));
    AdDto adResult = adService.createAd(minimumInfoAd);

    assertEquals(result, adResult);
  }

  @Test
  void pickupDateAfterDropoffDate() {
    ad.setPickupDate(LocalDate.of(2021, 3, 13));
    assertThrowsAdException();
  }

  @Test
  void dropoffStartTimeAfterDropoffEndTime() {
    ad.setDropoffTimeStart(LocalTime.of(18, 31));
    assertThrowsAdException();
  }

  @Test
  void pickupStartTimeAfterPickupEndTime() {
    ad.setPickupTimeStart(LocalTime.of(12, 31));
    assertThrowsAdException();
  }

  @Test
  void dropoffStartAfter4HoursPickupTimeEnd() {
    ad.setPickupTimeStart(LocalTime.of(14, 0));
    ad.setPickupTimeEnd(LocalTime.of(14, 30));
    ad.setDropoffTimeStart(LocalTime.of(16, 30));
    ad.setDropoffTimeEnd(LocalTime.of(17, 0));
    assertThrowsAdException();
  }

  @Test
  void missingAddress() {
    ad.setAddress(null);
    assertThrowsAdException();
  }

  @Test
  void missingPickupDate() {
    ad.setPickupDate(null);
    assertThrowsAdException();
  }

  @Test
  void missingPickupTimeStart() {
    ad.setPickupTimeStart(null);
    assertThrowsAdException();
  }

  @Test
  void missingPickupTimeEnd() {
    ad.setPickupTimeEnd(null);
    assertThrowsAdException();
  }

  @Test
  void missingDropoffDate() {
    ad.setDropoffDate(null);
    assertThrowsAdException();
  }

  @Test
  void missingDropoffTimeStart() {
    ad.setDropoffTimeStart(null);
    assertThrowsAdException();
  }

  @Test
  void missingDropoffTimeEnd() {
    ad.setDropoffTimeEnd(null);
    assertThrowsAdException();
  }

  @Test
  void missingZipCode() {
    ad.setZipcode(null);
    assertThrowsAdException();
  }

  @Test
  void missingClothingDesc() {
    ad.setClothingDescription(null);
    assertThrowsAdException();
  }

  @Test
  void missingPhoneNum() {
    ad.setPhoneNumber(null);
    assertThrowsAdException();
  }

  @Test
  void missingCreatorUsername() {
    ad.getCreator().setUsername(null);
    assertThrowsAdException();
  }

  private void assertThrowsAdException() {
    assertThrows(AdException.class, () -> adService.createAd(ad));
  }
}
