package washit.dto;

import java.time.LocalDateTime;

import washit.entity.Bid;

public class BidDto {
	private Integer adId;
	private String username;
	private Float amount;
	private LocalDateTime dateTimeCreated;
	private Boolean isAccepted;
	
	public static BidDto from (Bid bid) {
		BidDto result = new BidDto();
		
		result.setAdId(bid.getAdId().getId());
		result.setAmount(bid.getBidAmount());
		result.setDateTimeCreated(bid.getDateTimeCreated());
		result.setUsername(bid.getCreator().getUsername());
		result.setIsAccepted(bid.getIsAccepted());
		return result;
	}
	
	public BidDto() {
		
	}
	
	public BidDto(Integer adId, String username, Float amount, LocalDateTime dateTimeCreated, Boolean isAccepted ) {
		this.adId = adId;
		this.username = username;
		this.amount = amount;
		this.dateTimeCreated = dateTimeCreated;
		this.isAccepted = isAccepted;
	}


	public BidDto copy() {
		BidDto bidDto = new BidDto();
		bidDto.adId = this.adId;
		bidDto.username = this.username;
		bidDto.amount = this.amount;
		bidDto.dateTimeCreated = this.dateTimeCreated;
		bidDto.isAccepted = this.isAccepted;
		return bidDto;
	}

	public Boolean getIsAccepted() {
		return this.isAccepted;
	}

	public void setIsAccepted(Boolean isAccepted) {
		this.isAccepted = isAccepted;
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
}
