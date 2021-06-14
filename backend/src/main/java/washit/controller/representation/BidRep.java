package washit.controller.representation;

import java.time.LocalDateTime;

import washit.dto.BidDto;

public class BidRep {
	private Integer adId;
	private String username;
	private Float amount;
	private LocalDateTime dateTimeCreated;
	private Boolean isAccepted;

  public static BidDto toBidDto(BidRep bidRep) {
    BidDto bid = new BidDto();

    bid.setAdId(bidRep.getAdId());
    bid.setAmount(bidRep.getAmount());
    bid.setDateTimeCreated(bidRep.getDateTimeCreated());
    bid.setUsername(bidRep.getUsername());
    bid.setIsAccepted(bidRep.getIsAccepted());

    return bid;
  }

  public static BidRep fromBidDto(BidDto bidDto) {
    BidRep bidRep = new BidRep();
    
    bidRep.setAdId(bidDto.getAdId());
    bidRep.setUsername(bidDto.getUsername());
    bidRep.setAmount(bidDto.getAmount());
    bidRep.setDateTimeCreated(bidDto.getDateTimeCreated());
    bidRep.setIsAccepted(bidDto.getIsAccepted());
    return bidRep;
  }
  public Integer getAdId() {
		return adId;
	}

	public void setAdId(Integer adId) {
		this.adId = adId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public LocalDateTime getDateTimeCreated() {
		return dateTimeCreated;
	}

	public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
		this.dateTimeCreated = dateTimeCreated;
	}

	public Boolean getIsAccepted() {
		return this.isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
	}
}