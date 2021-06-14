package washit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import washit.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {

	Account findByUsername(String username);
	boolean existsByUsername(String username);
}
