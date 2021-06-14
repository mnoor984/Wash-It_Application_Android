package washit.dto;

import java.util.Objects;

public class AccountDto {

	private String username;

	public AccountDto() {
	}

	public AccountDto(String username, String email, String fullName, String password) {
		this.username = username;
		this.email = email;
  	this.fullName = fullName;
  	this.password = password;
  }

  public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	private String email;
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	private String fullName;
	public void setFullName(String fullName) {
		this.fullName=fullName;
	}
	
	public String getFullName() {
		return this.fullName;
	}

	private String password;
	public void setPassword(String password) {
		this.password=password;
	}
	
	public String getPassword() {
		return this.password;
	}

	private boolean loggedIn;

	public boolean isLoggedIn() {
		return this.loggedIn;
	}

	public void setLoggedIn(boolean status) {
		this.loggedIn = status;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final AccountDto that = (AccountDto) o;
		return loggedIn == that.loggedIn && username.equals(that.username) && email.equals(that.email) &&
					 fullName.equals(that.fullName) && password.equals(that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, email, fullName, password, loggedIn);
	}

	@Override
	public String toString() {
		return "AccountDto{" +
					 "username='" + username + '\'' +
					 ", email='" + email + '\'' +
					 ", fullName='" + fullName + '\'' +
					 ", password='" + password + '\'' +
					 ", loggedIn=" + loggedIn +
					 '}';
	}
}
