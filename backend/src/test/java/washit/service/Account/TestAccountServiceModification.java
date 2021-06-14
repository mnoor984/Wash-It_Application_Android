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
public class TestAccountServiceModification {

	@Mock
	private AccountRepository accRepo;

	@InjectMocks
	private AccountService service;

	private static final String EXISTING_USERNAME="ValidUserName";

	private static final String VALID_FULLNAME="John Smith";
	private static final String NEW_VALID_FULLNAME="Lucas Bluethner";
	private static final String EMPTY_FULLNAME="";
	private static final String INVALID_FULLNAME_TOOSHORT="s";
	private static final String INVALID_FULLNAME_TOOLONG="ThisIsAReallllllllllllllllllllllllllllllllllllllllllllllllllllllyFIRSTname";
	private static final String INVALID_FULLNAME_SPECIALCHAR="@dam";

	private static final String VALID_EMAIL="johnsmith@gmail.com";
	private static final String NEW_VALID_EMAIL="lucas.bluethner@mail.mcgill.ca";
	private static final String EMPTY_EMAIL="";
	private static final String INVALID_EMAIL="johnsmith@gmail";

	private static final String VALID_PASSWORD="Ecse428@000";
	private static final String NEW_VALID_PASSWORD="Password123!";
	private static final String EMPTY_PASSWORD="";
	private static final String INVALID_PASSWORD_TOOSHORT="ecse";
	private static final String INVALID_PASSWORD_TOOLONG="jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjEcse321@000";
	private static final String INVALID_PASSWORD_CONTAINSSPACE="Ecse428 @000";
	private static final String INVALID_PASSWORD_NOCAPITAL="ecse428@000";
	private static final String INVALID_PASSWORD_NOLOWER="ECSE428@000";
	private static final String INVALID_PASSWORD_NOSPECIALCHAR="Ecse428000";
	private static final String INVALID_PASSWORD_NONUMBER="ECSEeafd!ddeew@";

	
	@BeforeEach
	public void setUp() {

		lenient().when(accRepo.existsByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(EXISTING_USERNAME)) {
				return true;
			}
			else {
				return false;
			}
		});

		Account acc = new Account();
		acc.setUsername(EXISTING_USERNAME);
		acc.setFullName(VALID_FULLNAME);
		acc.setEmail(VALID_EMAIL);
		acc.setPassword(VALID_PASSWORD);

		lenient().when(accRepo.findByUsername(anyString())).thenReturn(acc);

		lenient().when(accRepo.save(Mockito.any(Account.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));
	}

	@Test
	public void testUpdateAccount() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			fail();
		}

		assertNotNull(acc);
		assertEquals(acc.getFullName(), NEW_VALID_FULLNAME);
		assertEquals(acc.getEmail(), NEW_VALID_EMAIL);
		assertEquals(acc.getPassword(), NEW_VALID_PASSWORD);
	}

	@Test
	public void testUpdateAccountEmptyFullName() {
		AccountDto dto = new AccountDto();

		dto.setFullName(EMPTY_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error  = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Account name cannot be empty");
	}

	@Test
	public void testUpdateAccountInvalidFullNameTooShort() {
		AccountDto dto = new AccountDto();

		dto.setFullName(INVALID_FULLNAME_TOOSHORT);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"First or last name has to be 2 and 20 characters inclusively");
	}

	@Test
	public void testUpdateAccountInvalidFullNameTooLong() {
		AccountDto dto = new AccountDto();

		dto.setFullName(INVALID_FULLNAME_TOOLONG);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"First or last name has to be 2 and 20 characters inclusively");
	}

	@Test
	public void testUpdateAccountInvalidFullNameSpecialChar() {
		AccountDto dto = new AccountDto();

		dto.setFullName(INVALID_FULLNAME_SPECIALCHAR);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"First or last name cannot contain special characters");
	}

	@Test
	public void testUpdateAccountEmptyEmail() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(EMPTY_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error  = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Account email cannot be empty");
	}

	@Test
	public void testUpdateAccountInvalidEmail() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(INVALID_EMAIL);
		dto.setPassword(NEW_VALID_PASSWORD);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Invalid email address");
	}

	@Test
	public void testUpdateAccountInvalidEmptyPassword() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(EMPTY_PASSWORD);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password cannot be empty");
	}

	@Test
	public void testUpdateAccountInvalidPasswordTooShort() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_TOOSHORT);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to be between 8 and 15 characters inclusively");
	}

	@Test
	public void testUpdateAccountInvalidPasswordTooLong() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_TOOLONG);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to be between 8 and 15 characters inclusively");
	}

	@Test
	public void testUpdateAccountInvalidPasswordContainsSpace() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_CONTAINSSPACE);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password cannot contain space");
	}

	@Test
	public void testUpdateAccountInvalidPasswordNoCapital() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOCAPITAL);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to contain at least one capital character");
	}

	@Test
	public void testUpdateAccountInvalidPasswordNoLower() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOLOWER);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to contain at least one lower character");
	}

	@Test
	public void testUpdateAccountInvalidPasswordNoSpecialChar() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOSPECIALCHAR);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to contain at least one special character");
	}

	@Test
	public void testUpdateAccountInvalidPasswordNoNumber() {
		AccountDto dto = new AccountDto();

		dto.setFullName(NEW_VALID_FULLNAME);
		dto.setEmail(NEW_VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NONUMBER);

		Account acc = null;
		String error = null;

		try {
			acc = service.updateAccount(EXISTING_USERNAME, dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}

		assertNull(acc);
		assertEquals(error,"Password has to contain at least one number");
	}
}
