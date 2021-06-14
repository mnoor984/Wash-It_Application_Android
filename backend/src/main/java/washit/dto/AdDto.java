package washit.dto;

import washit.entity.Ad;
import washit.entity.LaundryStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AdDto {
  private Integer id;
  private String address;
  private LocalDate pickupDate;
  private LocalTime pickupTimeStart;
  private LocalTime pickupTimeEnd;
  private LocalDate dropoffDate;
  private LocalTime dropoffTimeStart;
  private LocalTime dropoffTimeEnd;
  private Float weightKg;
  private String zipcode;
  private String clothingDescription;
  private String specialInstructions;
  private Boolean bleach;
  private Boolean iron;
  private Boolean fold;
  private String phoneNumber;
  private AccountDto creator;
  private LaundryStatus laundryStatus;

  public static AdDto from(Ad ad) {
    AdDto result = new AdDto();
    result.setId(ad.getId());
    result.setAddress(ad.getAddress());
    result.setPickupDate(ad.getPickupDate());
    result.setPickupTimeStart(ad.getPickupTimeStart());
    result.setPickupTimeEnd(ad.getPickupTimeEnd());
    result.setDropoffDate(ad.getDropoffDate());
    result.setDropoffTimeStart(ad.getDropoffTimeStart());
    result.setDropoffTimeEnd(ad.getDropoffTimeEnd());
    result.setWeightKg(ad.getWeightKg());
    result.setZipcode(ad.getZipcode());
    result.setClothingDescription(ad.getClothingDescription());
    result.setSpecialInstructions(ad.getSpecialInstructions());
    result.setBleach(ad.getBleach());
    result.setIron(ad.getIron());
    result.setFold(ad.getFold());
    result.setPhoneNumber(ad.getPhoneNumber());
    result.setCreator(new AccountDto(ad.getCreator().getUsername(),
                                     ad.getCreator().getEmail(),
                                     ad.getCreator().getFullName(),
                                     ad.getCreator().getPassword()));
    result.setLaundryStatus(ad.getLaundryStatus());
    return result;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public LocalDate getPickupDate() {
    return pickupDate;
  }

  public void setPickupDate(LocalDate pickupDate) {
    this.pickupDate = pickupDate;
  }

  public LocalTime getPickupTimeStart() {
    return pickupTimeStart;
  }

  public void setPickupTimeStart(LocalTime pickupTimeStart) {
    this.pickupTimeStart = pickupTimeStart;
  }

  public LocalTime getPickupTimeEnd() {
    return pickupTimeEnd;
  }

  public void setPickupTimeEnd(LocalTime pickupTimeEnd) {
    this.pickupTimeEnd = pickupTimeEnd;
  }

  public LocalDate getDropoffDate() {
    return dropoffDate;
  }

  public void setDropoffDate(LocalDate dropoffDate) {
    this.dropoffDate = dropoffDate;
  }

  public LocalTime getDropoffTimeStart() {
    return dropoffTimeStart;
  }

  public void setDropoffTimeStart(LocalTime dropoffTimeStart) {
    this.dropoffTimeStart = dropoffTimeStart;
  }

  public LocalTime getDropoffTimeEnd() {
    return dropoffTimeEnd;
  }

  public void setDropoffTimeEnd(LocalTime dropoffTimeEnd) {
    this.dropoffTimeEnd = dropoffTimeEnd;
  }

  public Float getWeightKg() {
    return weightKg;
  }

  public void setWeightKg(Float weightKg) {
    this.weightKg = weightKg;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getClothingDescription() {
    return clothingDescription;
  }

  public void setClothingDescription(String clothingDescription) {
    this.clothingDescription = clothingDescription;
  }

  public String getSpecialInstructions() {
    return specialInstructions;
  }

  public void setSpecialInstructions(String specialInstructions) {
    this.specialInstructions = specialInstructions;
  }

  public Boolean getBleach() {
    return bleach;
  }

  public void setBleach(Boolean bleach) {
    this.bleach = bleach;
  }

  public Boolean getIron() {
    return iron;
  }

  public void setIron(Boolean iron) {
    this.iron = iron;
  }

  public Boolean getFold() {
    return fold;
  }

  public void setFold(Boolean fold) {
    this.fold = fold;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public AccountDto getCreator() {
    return creator;
  }

  public void setCreator(AccountDto creator) {
    this.creator = creator;
  }
  
  public LaundryStatus getLaundryStatus() {
	  return laundryStatus;
  }
  
  public void setLaundryStatus(LaundryStatus status) {
	  this.laundryStatus = status;
  }

  public AdDto copy() {
    AdDto ad = new AdDto();
    ad.id = this.id;
    ad.address = this.address;
    ad.pickupDate = this.pickupDate;
    ad.pickupTimeStart = this.pickupTimeStart;
    ad.pickupTimeEnd = this.pickupTimeEnd;
    ad.dropoffDate = this.dropoffDate;
    ad.dropoffTimeStart = this.dropoffTimeStart;
    ad.dropoffTimeEnd = this.dropoffTimeEnd;
    ad.weightKg = this.weightKg;
    ad.zipcode = this.zipcode;
    ad.clothingDescription = this.clothingDescription;
    ad.specialInstructions = this.specialInstructions;
    ad.bleach = this.bleach;
    ad.iron = this.iron;
    ad.fold = this.fold;
    ad.phoneNumber = this.phoneNumber;
    ad.creator = this.creator;
    ad.laundryStatus = this.laundryStatus;
    return ad;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final AdDto adDto = (AdDto) o;
    return Objects.equals(id, adDto.id) && Objects.equals(address, adDto.address) &&
           Objects.equals(pickupDate, adDto.pickupDate) &&
           Objects.equals(pickupTimeStart, adDto.pickupTimeStart) &&
           Objects.equals(pickupTimeEnd, adDto.pickupTimeEnd) &&
           Objects.equals(dropoffDate, adDto.dropoffDate) &&
           Objects.equals(dropoffTimeStart, adDto.dropoffTimeStart) &&
           Objects.equals(dropoffTimeEnd, adDto.dropoffTimeEnd) &&
           Objects.equals(laundryStatus, adDto.laundryStatus) && 
           Objects.equals(weightKg, adDto.weightKg) && Objects.equals(zipcode, adDto.zipcode) &&
           Objects.equals(clothingDescription, adDto.clothingDescription) &&
           Objects.equals(specialInstructions, adDto.specialInstructions) &&
           Objects.equals(bleach, adDto.bleach) && Objects.equals(iron, adDto.iron) &&
           Objects.equals(fold, adDto.fold) && Objects.equals(phoneNumber, adDto.phoneNumber) &&
           Objects.equals(creator, adDto.creator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, address, pickupDate, pickupTimeStart, pickupTimeEnd, dropoffDate, dropoffTimeStart, dropoffTimeEnd, weightKg, zipcode, clothingDescription, laundryStatus, specialInstructions, bleach, iron, fold, phoneNumber);
  }

  @Override
  public String toString() {
    return "AdDto{" +
           "id=" + id +
           ", address='" + address + '\'' +
           ", pickupDate=" + pickupDate +
           ", pickupTimeStart=" + pickupTimeStart +
           ", pickupTimeEnd=" + pickupTimeEnd +
           ", dropoffDate=" + dropoffDate +
           ", dropoffTimeStart=" + dropoffTimeStart +
           ", dropoffTimeEnd=" + dropoffTimeEnd +
           ", weightKg=" + weightKg +
           ", zipcode='" + zipcode + '\'' +
           ", clothingDescription='" + clothingDescription + '\'' +
           ", specialInstructions='" + specialInstructions + '\'' +
           ", bleach=" + bleach +
           ", iron=" + iron +
           ", fold=" + fold +
           ", laundryStatus=" + laundryStatus +
           ", phoneNumber='" + phoneNumber + '\'' +
           ", creator=" + creator +
           '}';
  }
}
