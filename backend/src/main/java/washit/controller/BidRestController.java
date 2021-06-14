package washit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import washit.dto.BidDto;
import washit.entity.Ad;
import washit.entity.Bid;
import washit.service.BidService;
import washit.service.RequestAuthenticator;
import washit.service.exception.AccountException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@CrossOrigin(origins = "*")
@RestController()
public class BidRestController {

  private final BidService service;
  private final RequestAuthenticator authenticator;

  public BidRestController(BidService service, RequestAuthenticator authenticator) {
    this.service = service;
    this.authenticator = authenticator;
  }

  @GetMapping(value = {"/bids/{adId}", "/bids/{adId}/"})
  public ResponseEntity<List<BidDto>> getAllBids(@PathVariable("adId") int adId) {
    List<BidDto> bidDtos = new ArrayList<>(service.getAllBids(adId));
    return new ResponseEntity<>(bidDtos, HttpStatus.OK);
  }

  @PutMapping(value = {"/bids/{adId}/{username}", "/bids/{adId}/{username}/"})
  public ResponseEntity<BidDto> acceptBid(@PathVariable("adId") int adId, @PathVariable("username") String username) {
   BidDto response = service.acceptBid(adId, username);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
  
  @PostMapping(value = {"/bids", "/bids/"})
  public ResponseEntity<BidDto> createBid(@RequestBody BidDto dto, @RequestHeader Map<String, String> headers) {
    // authorization
    String tokenUsername = authenticator.getUsernameFromHeaders(headers);
    if (!dto.getUsername().equals(tokenUsername))
      throw new AccountException("Cannot make a bid for an account you are not logged into");

    Bid bid = service.createBid(dto);
    return new ResponseEntity<>(convertToDto(bid), HttpStatus.CREATED);
  }

  private BidDto convertToDto(Bid bid) {
    BidDto bidDto = new BidDto();
    bidDto.setAmount(bid.getBidAmount());
    bidDto.setDateTimeCreated(bid.getDateTimeCreated());
    bidDto.setUsername(bid.getCreator().getUsername());
    return bidDto;
  }

}
