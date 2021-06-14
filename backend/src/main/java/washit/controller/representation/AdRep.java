package washit.controller.representation;

import washit.dto.AccountDto;
import washit.dto.AdDto;
import washit.entity.LaundryStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class AdRep {
  private String id;
  private String address;
  private String pickupStart;
  private String pickupEnd;
  private String dropoffStart;
  private String dropoffEnd;
  private Float weight;
  private String zipcode;
  private String clothingDesc;
  private String specialInst;
  private Boolean bleach;
  private Boolean iron;
  private Boolean fold;
  private String phoneNum;
  private String account;
  private LaundryStatus laundryStatus;

  public static AdDto toAdDto(AdRep adRep) {
    AdDto ad = new AdDto();
    try {
      ad.setId(Integer.parseInt(adRep.getId()));
    } catch (NumberFormatException e) {
      ad.setId(null);
    }
    ad.setAddress(adRep.getAddress());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    if (adRep.pickupStart != null && adRep.pickupEnd != null) {
      LocalDateTime pickupStart = LocalDateTime.parse(adRep.getPickupStart(), formatter);
      LocalDateTime pickupEnd = LocalDateTime.parse(adRep.getPickupEnd(), formatter);
      ad.setPickupDate(pickupStart.toLocalDate());
      ad.setPickupTimeStart(pickupStart.toLocalTime());
      ad.setPickupTimeEnd(pickupEnd.toLocalTime());
    }
    if (adRep.dropoffStart != null && adRep.dropoffEnd != null) {
      LocalDateTime dropoffStart = LocalDateTime.parse(adRep.getDropoffStart(), formatter);
      LocalDateTime dropoffEnd = LocalDateTime.parse(adRep.getDropoffEnd(), formatter);
      ad.setDropoffDate(dropoffStart.toLocalDate());
      ad.setDropoffTimeStart(dropoffStart.toLocalTime());
      ad.setDropoffTimeEnd(dropoffEnd.toLocalTime());
    }
    ad.setWeightKg(adRep.getWeight());
    if (adRep.zipcode != null) {
      ad.setZipcode(adRep.getZipcode().replace("-", ""));
    }
    ad.setClothingDescription(adRep.getClothingDesc());
    ad.setSpecialInstructions(adRep.getSpecialInst());
    ad.setBleach(adRep.getBleach());
    ad.setIron(adRep.getIron());
    ad.setFold(adRep.getFold());
    ad.setPhoneNumber(adRep.getPhoneNum());
    ad.setCreator(new AccountDto(adRep.getAccount(), null, null, null));
    ad.setLaundryStatus(adRep.getLaundryStatus());
    return ad;
  }

  public static AdRep fromAdDto(AdDto adDto) {
    AdRep ad = new AdRep();
    ad.setId(String.valueOf(adDto.getId()));
    ad.setAddress(adDto.getAddress());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    LocalDateTime pickupStart = LocalDateTime.of(adDto.getPickupDate(), adDto.getPickupTimeStart());
    LocalDateTime pickupEnd = LocalDateTime.of(adDto.getPickupDate(), adDto.getPickupTimeEnd());
    LocalDateTime dropoffStart = LocalDateTime.of(adDto.getDropoffDate(), adDto.getDropoffTimeStart());
    LocalDateTime dropoffEnd = LocalDateTime.of(adDto.getDropoffDate(), adDto.getDropoffTimeEnd());
    ad.setPickupStart(pickupStart.format(formatter));
    ad.setPickupEnd(pickupEnd.format(formatter));
    ad.setDropoffStart(dropoffStart.format(formatter));
    ad.setDropoffEnd(dropoffEnd.format(formatter));
    ad.setWeight(adDto.getWeightKg());
    ad.setZipcode(adDto.getZipcode());
    ad.setClothingDesc(adDto.getClothingDescription());
    ad.setSpecialInst(adDto.getSpecialInstructions());
    ad.setBleach(adDto.getBleach());
    ad.setIron(adDto.getIron());
    ad.setFold(adDto.getFold());
    ad.setPhoneNum(adDto.getPhoneNumber());
    ad.setAccount(adDto.getCreator().getUsername());
    ad.setLaundryStatus(adDto.getLaundryStatus());
    return ad;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPickupStart() {
    return pickupStart;
  }

  public void setPickupStart(String pickupStart) {
    this.pickupStart = pickupStart;
  }

  public String getPickupEnd() {
    return pickupEnd;
  }

  public void setPickupEnd(String pickupEnd) {
    this.pickupEnd = pickupEnd;
  }

  public Float getWeight() {
    return weight;
  }

  public void setWeight(Float weight) {
    this.weight = weight;
  }

  public String getZipcode() {
    return zipcode;
  }

  public void setZipcode(String zipcode) {
    this.zipcode = zipcode;
  }

  public String getClothingDesc() {
    return clothingDesc;
  }

  public void setClothingDesc(String clothingDesc) {
    this.clothingDesc = clothingDesc;
  }

  public String getSpecialInst() {
    return specialInst;
  }

  public void setSpecialInst(String specialInst) {
    this.specialInst = specialInst;
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

  public String getPhoneNum() {
    return phoneNum;
  }

  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public String getDropoffStart() {
    return dropoffStart;
  }

  public void setDropoffStart(String dropoffStart) {
    this.dropoffStart = dropoffStart;
  }

  public String getDropoffEnd() {
    return dropoffEnd;
  }

  public void setDropoffEnd(String dropoffEnd) {
    this.dropoffEnd = dropoffEnd;
  }
  
  public LaundryStatus getLaundryStatus() {
	  return laundryStatus;
  }
  
  public void setLaundryStatus(LaundryStatus status) {
	  this.laundryStatus = status;
  }
}
