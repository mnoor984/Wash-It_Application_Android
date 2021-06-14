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
import washit.util.JwtUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestAccountServiceLogin {
	
	@Mock
	private AccountRepository accRepo;
	
	@Mock
	private JwtUtil jwtUtil;;

	@InjectMocks
	private AccountService service;
	
	private static final String EXISTING_USERNAME="ValidUserName";
	private static final String NONEXISTING_USERNAME="InexistentUserName";
	
	private static final String VALID_PASSWORD="Password123!";
	private static final String INVALID_PASSWORD="Password124!";
	
	private static final String VALID_FULLNAME="Lebron James";
	private static final String VALID_EMAIL="Lebron@Lakers.com";
	
	@BeforeEach
	public void setUpMock() {
		
		Account account = new Account();
		account.setUsername(EXISTING_USERNAME);
		account.setFullName(VALID_FULLNAME);
		account.setEmail(VALID_EMAIL);
		account.setPassword(VALID_PASSWORD);

		lenient().when(accRepo.findByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(EXISTING_USERNAME)) {
				return account;
			}
			else {
				return null;
			}
		});
		
		lenient().when(jwtUtil.generateToken(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(EXISTING_USERNAME)) {
				return EXISTING_USERNAME;
			}
			else {
				return null;
			}
		});

	}
	
	@Test
	public void testLoginAccountValid() {
		AccountDto dto = new AccountDto();

		dto.setUsername(EXISTING_USERNAME);
		dto.setPassword(VALID_PASSWORD);

		String username = null;

		try {
			username = service.loginToAccount(dto);
		}
		catch(AccountException e) {
			fail();
		}
		System.out.println(username);
		System.out.println(EXISTING_USERNAME);
		assertEquals(username, EXISTING_USERNAME);

	}
	
	@Test
	public void testLoginAccountInvalidUser() {
		AccountDto dto = new AccountDto();

		dto.setUsername(NONEXISTING_USERNAME);
		dto.setPassword(VALID_PASSWORD);

		String username = null;
		String error  = null;

		try {
			username = service.loginToAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(username);
		assertEquals(error,"Account does not exist");

	}
	
	@Test
	public void testLoginAccountInvalidPass() {
		AccountDto dto = new AccountDto();

		dto.setUsername(EXISTING_USERNAME);
		dto.setPassword(INVALID_PASSWORD);

		String username = null;
		String error  = null;

		try {
			username = service.loginToAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(username);
		assertEquals(error,"Incorrect password");

	}

}
