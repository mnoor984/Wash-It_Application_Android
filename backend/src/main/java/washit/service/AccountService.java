package washit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import washit.dao.AccountRepository;
import washit.dao.AdRepository;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.service.exception.AccountException;
import washit.util.JwtUtil;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {

	AccountRepository accRepo;
	AdRepository adRepo;
	JwtUtil jwtUtil;

	public AccountService(@Autowired AccountRepository accRepo,
												@Autowired AdRepository adRepo,
												@Autowired JwtUtil jwtUtil) {
		this.accRepo = accRepo;
		this.adRepo = adRepo;
		this.jwtUtil = jwtUtil;
	}

	@Transactional
	public String loginToAccount(AccountDto providedAccountDetails) {
		Account account = accRepo.findByUsername(providedAccountDetails.getUsername());
		// validation checks
		if (account == null)
			throw new AccountException("Account does not exist");
		if (!account.getPassword().equals(providedAccountDetails.getPassword()))
			throw new AccountException("Incorrect password");

		//account.setLoggedIn(true);
		// create a new token
		return jwtUtil.generateToken(account.getUsername());
	}

	@Transactional
	public Account findAccountByUsername(String username) throws AccountException {

		if (!accRepo.existsByUsername(username)) {
			throw new AccountException("No Account with username [" + username + "] exists");
		}
		return accRepo.findByUsername(username);
	}

	@Transactional
	public List<Account> getAllAccounts() throws AccountException{

		List<Account> allAcc = new ArrayList<Account>();

		if (accRepo.count()==0) {
			throw new AccountException("No Accounts found in system");
		}
		else {
			for (Account acc:accRepo.findAll()) {
				allAcc.add(acc);
			}
		}
		return allAcc;

	}

	@Transactional
	public Account createAccount(AccountDto dto) throws AccountException {

		String username=dto.getUsername();
		String fullName=dto.getFullName();
		String email=dto.getEmail();
		String password=dto.getPassword();

		if(accRepo.existsByUsername(username)) {
			throw new AccountException("Account with username ["+username+"] exists");
		}

		// empty username
		if (username.equals("")) {
			throw new AccountException("Account username cannot be empty");
		}
		// invalid username
		try {
			isUsernameValid(username);
		}
		catch(IllegalArgumentException e){
			throw new AccountException(e.getMessage());
		}

		//empty first name
		if (fullName.equals("")) {
			throw new AccountException("Account name cannot be empty");
		}
		// invalid first name
		try {
			isNameValid(fullName);
		}
		catch(IllegalArgumentException e){
			throw new AccountException(e.getMessage());
		}

		//empty email
		if (email.equals("")) {
			throw new AccountException("Account email cannot be empty");
		}
		//invalid email check
		String emailRegexTemplate="^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		if (!email.matches(emailRegexTemplate)) {
			throw new AccountException("Invalid email address");
		}

		//empty password
		if (password.equals("")) {
			throw new AccountException("Password cannot be empty");
		}
		//invalid password
		try {
			isPasswordValid(password);
		}
		catch(IllegalArgumentException e){
			throw new AccountException(e.getMessage());
		}
		//otherwise create account
		Account acc = new Account();
		acc.setUsername(username);
		acc.setFullName(fullName);
		acc.setEmail(email);
		acc.setPassword(password);
		acc.setLoggedIn(false);

		//save to repo
		//getAllAccounts().add(acc);
		acc = accRepo.save(acc);
		return acc;
	}

	@Transactional
	public Account updateAccount(String username, AccountDto dto) throws AccountException{

		String newFullName = dto.getFullName();
		String newEmail = dto.getEmail();
		String newPassword = dto.getPassword();

		// Account info validation is pretty much copied from createAccount above.
		// Should probably put the account info validation into its own function for reusability?
		if (!accRepo.existsByUsername(username)) {
			throw new AccountException("No Account with username ["+username+"] exists");
		}

		// empty first name check
		if (newFullName.equals("")) {
			throw new AccountException("Account name cannot be empty");
		}

		// invalid first name check
		try {
			isNameValid(newFullName);
		}
		catch(IllegalArgumentException e){
			throw new AccountException(e.getMessage());
		}

		// empty email check
		if (newEmail.equals("")) {
			throw new AccountException("Account email cannot be empty");
		}

		// invalid email check
		String emailRegexTemplate="^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		if (!newEmail.matches(emailRegexTemplate)) {
			throw new AccountException("Invalid email address");
		}

		// empty password check
		if (newPassword.equals("")) {
			throw new AccountException("Password cannot be empty");
		}

		// invalid password check
		try {
			isPasswordValid(newPassword);
		}
		catch(IllegalArgumentException e){
			throw new AccountException(e.getMessage());
		}

		// All is valid! Update account info
		Account acc = accRepo.findByUsername(username);

		acc.setFullName(newFullName);
		acc.setEmail(newEmail);
		acc.setPassword(newPassword);

		acc = accRepo.save(acc);

		return acc;
	}

	@Transactional
	public void deleteAccountByUsername(String username) throws AccountException{

		if (!accRepo.existsByUsername(username)) {
			throw new AccountException("No account with username \""+username+"\" exists");
		}

		Account acc = accRepo.findByUsername(username);

		accRepo.delete(acc);
	}


	//helper methods
	private void isNameValid(String name) {

		// btwn 2 and 25 chars
		// no special character

		if (!((name.length() >= 2) && (name.length() <= 25))) {
			throw new IllegalArgumentException("First or last name has to be 2 and 20 characters inclusively");
		}

		// for special characters
		if ((name.contains("@") || name.contains("#") || name.contains("!") || name.contains("~")
				|| name.contains("$") || name.contains("%") || name.contains("^") || name.contains("&")
				|| name.contains("*") || name.contains("(") || name.contains(")") || name.contains("-")
				|| name.contains("+") || name.contains("/") || name.contains(":") || name.contains(".")
				|| name.contains(", ") || name.contains("<") || name.contains(">") || name.contains("?")
				|| name.contains("|"))) {
			throw new IllegalArgumentException("First or last name cannot contain special characters");
		}
	}

	private void isUsernameValid(String username) {

		// btwn 5 and 20 chars
		// no space
		// no special character

		if (!((username.length() >= 5) && (username.length() <= 20))) {
			throw new IllegalArgumentException("Username has to be 5 and 20 characters inclusively");
		}

		// to check space
		if (username.contains(" ")) {
			throw new IllegalArgumentException("Username cannot contain space");
		}

		// for special characters
		if ((username.contains("@") || username.contains("#") || username.contains("!") || username.contains("~")
				|| username.contains("$") || username.contains("%") || username.contains("^") || username.contains("&")
				|| username.contains("*") || username.contains("(") || username.contains(")") || username.contains("-")
				|| username.contains("+") || username.contains("/") || username.contains(":") || username.contains(".")
				|| username.contains(", ") || username.contains("<") || username.contains(">") || username.contains("?")
				|| username.contains("|"))) {
			throw new IllegalArgumentException("Username cannot contain special characters");
		}
	}

	private void isPasswordValid(String password) {

		// btwn 8 and 15 chars
		// no space
		// at least 1 num
		// at least 1 special char
		// at least 1 capital
		// at least 1 lower case

		if (!((password.length() >= 8) && (password.length() <= 15))) {
			throw new IllegalArgumentException("Password has to be between 8 and 15 characters inclusively");
		}

		// to check space
		if (password.contains(" ")) {
			throw new IllegalArgumentException("Password cannot contain space");
		}

		int numberCount = 0;
		for (int i = 0; i <= 9; i++) {
			String str1 = Integer.toString(i);

			if (password.contains(str1)) {
				numberCount = 1;
				break;
			}
		}
		if (numberCount == 0) {
			throw new IllegalArgumentException("Password has to contain at least one number");
		}

		// for special characters
		if (!(password.contains("@") || password.contains("#") || password.contains("!") || password.contains("~")
				|| password.contains("$") || password.contains("%") || password.contains("^") || password.contains("&")
				|| password.contains("*") || password.contains("(") || password.contains(")") || password.contains("-")
				|| password.contains("+") || password.contains("/") || password.contains(":") || password.contains(".")
				|| password.contains(", ") || password.contains("<") || password.contains(">") || password.contains("?")
				|| password.contains("|"))) {
			throw new IllegalArgumentException("Password has to contain at least one special character");
		}

		// checking capital letters
		int capitalCount = 0;
		for (int i = 65; i <= 90; i++) {

			// type casting
			char c = (char) i;

			String str1 = Character.toString(c);
			if (password.contains(str1)) {
				capitalCount = 1;
				break;
			}
		}
		if (capitalCount == 0) {
			throw new IllegalArgumentException("Password has to contain at least one capital character");
		}

		int lowerCount = 0;

		// checking small letters
		for (int i = 90; i <= 122; i++) {

			// type casting
			char c = (char) i;
			String str1 = Character.toString(c);

			if (password.contains(str1)) {
				lowerCount = 1;
				break;
			}
		}
		if (lowerCount == 0) {
			throw new IllegalArgumentException("Password has to contain at least one lower character");
		}
	}

}
