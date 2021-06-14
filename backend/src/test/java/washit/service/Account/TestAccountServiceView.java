package washit.service.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.service.AccountService;
import washit.service.exception.AccountException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestAccountServiceView {
	
	@Mock
	private AccountRepository accRepo;

	@InjectMocks
	private AccountService service;
	
	private static final String EXISTING_USERNAME="Lebron23";
	
	private static final String NONEXISTING_USERNAME="Jordan23";
	
	private static final String FULLNAME="Lebron James";
	private static final String PASSWORD="Password123!";
	private static final String EMAIL="lbj@lakers.com";
	
	@BeforeEach
	public void setUp() {
		
		Account account = new Account();
		account.setUsername(EXISTING_USERNAME);
		account.setFullName(FULLNAME);
		account.setEmail(EMAIL);
		account.setPassword(PASSWORD);
		
		lenient().when(accRepo.existsByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(EXISTING_USERNAME)) {
				return true;
			}
			else {
				return false;
			}
		});
		
		lenient().when(accRepo.findByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(EXISTING_USERNAME)) {
				return account;
			}
			else {
				return null;
			}
		});
		
	}
	
	@Test
	public void testViewAccount() {
		
		Account account = null;

		try {
			account = service.findAccountByUsername(EXISTING_USERNAME);
		}
		catch(AccountException e) {
			fail();
		}

		assertNotNull(account);
		assertEquals(account.getFullName(), FULLNAME);
		assertEquals(account.getEmail(), EMAIL);
		assertEquals(account.getPassword(), PASSWORD);
		
	}
	
	@Test
	public void testViewAccountInvalidUsername() {


		Account account = null;
		String error = null;

		try {
			account = service.findAccountByUsername(NONEXISTING_USERNAME);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(account);
		assertEquals(error,"No Account with username [" + NONEXISTING_USERNAME + "] exists");
	}

}
