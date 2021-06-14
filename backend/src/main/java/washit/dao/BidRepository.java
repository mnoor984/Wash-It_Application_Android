package washit.dao;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import washit.entity.Account;
import washit.entity.Ad;
import washit.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Bid.BidPK>{
	List<Bid> findByAdId(Ad adId);
	List<Bid> findByCreator(Account creator);
	
	Optional<Bid> findByAdIdAndCreatorUsername(Ad adId, String username);
	
	
}
