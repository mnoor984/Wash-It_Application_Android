package washit.service.Account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import washit.dao.AccountRepository;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.service.AccountService;
import washit.service.exception.AccountException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestAccountServiceCreation {

	@Mock
	private AccountRepository accRepo;
	
	@InjectMocks
	private AccountService service;
	
	private static final String VALID_USERNAME = "ValidUserName";
	private static final String NO_USERNAME="";
	private static final String INVALID_USERNAMETOOSHORT="sho";
	private static final String INVALID_USERNAMETOOLONG="ThisIsAReallllllllllllllllllllllllllllllllllllllllllllllllyLongUesrname";
	private static final String INVALID_USERNAMESPECIALCHAR="Invalid@User";
	private static final String INVALID_USERNAMEDUPLICATE="ValidUser";
	private static final String INVALID_USERNAMESPACE="INValid User";

	
	private static final String VALID_FULLNAME = "John Smith";
	private static final String NO_FULLNAME="";
	private static final String INVALID_FULLNAMETOOSHORT="s";
	private static final String INVALID_FULLNAMETOOLONG="ThisIsAReallllllllllllllllllllllllllllllllllllllllllllllllllllllyFIRSTname";
	private static final String INVALID_FULLNAMESPECIALCHAR="@dam";

	private static final String VALID_EMAIL="johnsmith@gmail.com";
	private static final String INVALID_EMAIL="johnsmith@gmail";

	private static final String VALID_PASSWORD="Ecse321@000";
	private static final String INVALID_PASSWORD_SHORT="ecse";
	private static final String INVALID_PASSWORD_LONG="jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjEcse321@000";
	private static final String INVALID_PASSWORD_SPACE="Ecse321 @000";
	private static final String INVALID_PASSWORD_NOCAPITAL="ecse321@000";
	private static final String INVALID_PASSWORD_NOSPECIAL="Ecse321000";
	private static final String INVALID_PASSWORD_NOLOWER="ECSE321@000";
	private static final String INVALID_PASSWORD_NONUMBER="ECSEeafd!ddeew@";
	
	@BeforeEach
	public void setMockOutput() {
		
		Answer<?> paramAsAnswer = (InvocationOnMock invocation)->{
			return invocation.getArgument(0);
		};
		
		//lenient().when(accRepo.save(any(Account.class))).thenAnswer(paramAsAnswer);
		lenient().when(accRepo.existsByUsername(anyString())).thenAnswer((InvocationOnMock invocation)->{
			if (invocation.getArgument(0).equals(INVALID_USERNAMEDUPLICATE)) {
				return true;
			}
			else {
				return false;
			} 
		});

		lenient().when(accRepo.save(Mockito.any(Account.class))).thenAnswer((InvocationOnMock i) -> i.getArgument(0));
	}
	@Test
	public void createValidAccount() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			fail();
		}
		assertNotNull(acc);
	}
	@Test
	public void createAccountEmptyUsername() {
		AccountDto dto = new AccountDto();
		dto.setUsername(NO_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Account username cannot be empty");
	}
	@Test
	public void createAccountInvalidUsernameTooShort() {
		AccountDto dto = new AccountDto();
		dto.setUsername(INVALID_USERNAMETOOSHORT);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Username has to be 5 and 20 characters inclusively");
	}
	@Test
	public void createAccountInvalidUsernameTooLong() {
		AccountDto dto = new AccountDto();
		dto.setUsername(INVALID_USERNAMETOOLONG);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Username has to be 5 and 20 characters inclusively");
	}
	@Test
	public void createAccountInvalidUsernameSpecialChar() {
		AccountDto dto = new AccountDto();
		dto.setUsername(INVALID_USERNAMESPECIALCHAR);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Username cannot contain special characters");
	}
	@Test
	public void createAccountInvalidUsernameSpace() {
		AccountDto dto = new AccountDto();
		dto.setUsername(INVALID_USERNAMESPACE);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Username cannot contain space");
	}
	@Test
	public void createAccountInvalidUsernameDuplicate() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		AccountDto dto2 = new AccountDto();
		dto2.setUsername(INVALID_USERNAMEDUPLICATE);
		dto2.setFullName(VALID_FULLNAME);
		dto2.setEmail(VALID_EMAIL);
		dto2.setPassword(VALID_PASSWORD);

		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			fail();
		}

		assertNotNull(acc);

		Account acc2=null;
		try {
			acc2 = service.createAccount(dto2);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc2);
		assertEquals(error,"Account with username ["+INVALID_USERNAMEDUPLICATE+"] exists");
	}
	@Test
	public void createAccountFullNameEmpty() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(NO_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Account name cannot be empty");
	}
	@Test
	public void createAccountInvalidFullNameTooShort() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(INVALID_FULLNAMETOOSHORT);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"First or last name has to be 2 and 20 characters inclusively");
	}
	@Test
	public void createAccountInvalidFullNameTooLong() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(INVALID_FULLNAMETOOLONG);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"First or last name has to be 2 and 20 characters inclusively");
	}
	@Test
	public void createAccountInvalidFullNameSpecialChar() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(INVALID_FULLNAMESPECIALCHAR);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"First or last name cannot contain special characters");
	}
	@Test
	public void createAccountEmptyEmail() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail("");
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Account email cannot be empty");
	}
	@Test
	public void createAccountInvalidEmail() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(INVALID_EMAIL);
		dto.setPassword(VALID_PASSWORD);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Invalid email address");
	}
	@Test
	public void createAccountInvalidPasswordTooShort() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_SHORT);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to be between 8 and 15 characters inclusively");
	}
	@Test
	public void createAccountInvalidPasswordTooLong() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_LONG);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to be between 8 and 15 characters inclusively");
	}
	@Test
	public void createAccountInvalidPasswordSpace() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_SPACE);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password cannot contain space");
	}
	@Test
	public void createAccountInvalidPasswordNoCapital() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOCAPITAL);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to contain at least one capital character");
	}
	@Test
	public void createAccountInvalidPasswordNoSpecialChar() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOSPECIAL);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to contain at least one special character");
	}
	@Test
	public void createAccountInvalidPasswordNoLower() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NOLOWER);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to contain at least one lower character");
	}
	@Test
	public void createAccountInvalidPasswordNoNum() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword(INVALID_PASSWORD_NONUMBER);
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password has to contain at least one number");
	}
	@Test
	public void createAccountInvalidPasswordEmpty() {
		AccountDto dto = new AccountDto();
		dto.setUsername(VALID_USERNAME);
		dto.setFullName(VALID_FULLNAME);
		dto.setEmail(VALID_EMAIL);
		dto.setPassword("");
		
		Account acc=null;
		String error = null;
		try {
			acc = service.createAccount(dto);
		}
		catch(AccountException e) {
			error = e.getMessage();
		}
		
		assertNull(acc);
		assertEquals(error,"Password cannot be empty");
	}
}
