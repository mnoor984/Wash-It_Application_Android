package washit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import washit.entity.Ad;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Integer> {

	Optional<Ad> findAdById(Integer id);
	List<Ad> findAdsByOrderByZipcodeAsc();
	List<Ad> findAdsByOrderByZipcodeDesc();
	
}
