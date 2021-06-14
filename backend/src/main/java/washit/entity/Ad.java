package washit.entity;

import washit.dto.AccountDto;
import washit.dto.AdDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ad")
public class Ad implements Serializable {

  private static final long serialVersionUID = 3611576185014561975L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ad_id")
  private Integer id;

  @Column(name = "address")
  private String address;

  @Column(name = "pickup_date")
  private LocalDate pickupDate;

  @Column(name = "pickup_time_start")
  private LocalTime pickupTimeStart;

  @Column(name = "pickup_time_end")
  private LocalTime pickupTimeEnd;

  @Column(name = "dropoff_date")
  private LocalDate dropoffDate;

  @Column(name = "dropoff_time_start")
  private LocalTime dropoffTimeStart;

  @Column(name = "dropoff_time_end")
  private LocalTime dropoffTimeEnd;

  @Column(name = "weight_kg")
  private Float weightKg;

  @Column(name = "zipcode")
  private String zipcode;

  @Column(name = "clothing_desc")
  private String clothingDescription;

  @Column(name = "special_instr")
  private String specialInstructions;

  @Column(name = "bleach")
  private Boolean bleach;

  @Column(name = "iron")
  private Boolean iron;

  @Column(name = "fold")
  private Boolean fold;

  @Column(name = "phone_num")
  private String phoneNumber;

  @Column(name = "laundry_status")
  @Enumerated(EnumType.STRING)
  private LaundryStatus laundryStatus = LaundryStatus.POSTED;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "creator")
  private Account creator;

  @OneToMany(cascade = CascadeType.REMOVE,
    mappedBy = "adId"
  )
  private final List<Bid> bids = new ArrayList<>(); // DO NOT provide getters, setters, or add to constructor

  public static Ad from(AdDto adDto) {
    Ad ad = new Ad();
    ad.id = adDto.getId();
    ad.address = adDto.getAddress();
    ad.pickupDate = adDto.getPickupDate();
    ad.pickupTimeStart = adDto.getPickupTimeStart();
    ad.pickupTimeEnd = adDto.getPickupTimeEnd();
    ad.dropoffDate = adDto.getDropoffDate();
    ad.dropoffTimeStart = adDto.getDropoffTimeStart();
    ad.dropoffTimeEnd = adDto.getDropoffTimeEnd();
    ad.weightKg = adDto.getWeightKg();
    ad.zipcode = adDto.getZipcode();
    ad.clothingDescription = adDto.getClothingDescription();
    ad.specialInstructions = adDto.getSpecialInstructions();
    ad.bleach = adDto.getBleach();
    ad.iron = adDto.getIron();
    ad.fold = adDto.getFold();
    ad.phoneNumber = adDto.getPhoneNumber();
    AccountDto creator = adDto.getCreator();
    ad.creator = new Account(creator.getUsername(), creator.getEmail(), creator.getFullName(), creator.getPassword(),
                             creator.isLoggedIn());
    return ad;
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
  
  public LaundryStatus getLaundryStatus() {
	  return laundryStatus;
  }
  
  public void setLaundryStatus(LaundryStatus status) {
	  this.laundryStatus = status;
  }

  public Account getCreator() {
    return creator;
  }
    
  public void setCreator(Account creator) {
    this.creator = creator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Ad ad = (Ad) o;
    return Objects.equals(id, ad.id) && Objects.equals(address, ad.address) &&
           Objects.equals(pickupDate, ad.pickupDate) &&
           Objects.equals(pickupTimeStart, ad.pickupTimeStart) &&
           Objects.equals(pickupTimeEnd, ad.pickupTimeEnd) &&
           Objects.equals(dropoffDate, ad.dropoffDate) &&
           Objects.equals(dropoffTimeStart, ad.dropoffTimeStart) &&
           Objects.equals(dropoffTimeEnd, ad.dropoffTimeEnd) &&
           Objects.equals(laundryStatus, ad.laundryStatus) &&
           Objects.equals(weightKg, ad.weightKg) && Objects.equals(zipcode, ad.zipcode) &&
           Objects.equals(clothingDescription, ad.clothingDescription) &&
           Objects.equals(specialInstructions, ad.specialInstructions) &&
           Objects.equals(bleach, ad.bleach) && Objects.equals(iron, ad.iron) &&
           Objects.equals(fold, ad.fold) && Objects.equals(phoneNumber, ad.phoneNumber) &&
           Objects.equals(creator, ad.creator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "Ad{" +
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
