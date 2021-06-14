package ca.mcgill.ecse428.application.dto;

import java.time.LocalDateTime;

public class BidDto {
    private Integer adId;
    private String username;
    private Float amount;
    private String dateTimeCreated;
    private boolean isAccepted;

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
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

    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    @Override
    public String toString() {
        return "BidDto{" +
                "adId=" + adId +
                ", username='" + username + '\'' +
                ", amount=" + amount +
                ", dateTimeCreated=" + dateTimeCreated +
                "}\n";
    }
}
