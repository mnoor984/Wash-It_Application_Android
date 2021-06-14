package washit.entity;

import washit.dto.AccountDto;
import washit.dto.BidDto;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bid")
@IdClass(Bid.BidPK.class)
public class Bid implements Serializable {

  private static final long serialVersionUID = -2194161894630643140L;

  public static class BidPK implements Serializable {

    private static final long serialVersionUID = -8257254897261169719L;

    private Integer adId;
    private String creator;

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      final BidPK that = (BidPK) o;
      return adId.equals(that.adId) && creator.equals(that.creator);
    }

    @Override
    public int hashCode() {
      return Objects.hash(adId, creator);
    }
  }

  public Bid() {
  }

  public Bid(Ad adId, Account creator, Float bidAmount, LocalDateTime dateTimeCreated, Boolean isAccepted) {
    this.adId = adId;
    this.creator = creator;
    this.bidAmount = bidAmount;
    this.dateTimeCreated = dateTimeCreated;
    this.isAccepted = isAccepted;
  }

  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "ad_id")
  private Ad adId;

  @Id
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "creator_username")
  private Account creator;

  @Column(name = "bid_amount")
  private Float bidAmount;

  @Column(name = "date_created")
  private LocalDateTime dateTimeCreated;

  @Column(name = "is_accepted")
  private Boolean isAccepted;

  public Boolean getIsAccepted() {
    return this.isAccepted;
  }

  public void setIsAccepted(Boolean isAccepted) {
    this.isAccepted = isAccepted;
  }

  public Ad getAdId() {
	  return this.adId;
  }
  
  public void setAdId(Ad anAdId) {
	  this.adId = anAdId;
  }

  public Account getCreator() {
	  return this.creator;  
  }
  
  public void setCreator(Account creator) {
	  this.creator = creator;
  }
  
  public Float getBidAmount() {
	  return this.bidAmount;
  }
  
  public void setBidAmount(Float amount) {
	  this.bidAmount = amount;
  }
  
  public LocalDateTime getDateTimeCreated() {
	  return this.dateTimeCreated;
  }
  
  public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
	  this.dateTimeCreated = dateTimeCreated; 
  }
}