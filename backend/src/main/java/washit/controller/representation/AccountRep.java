package washit.controller.representation;

import washit.dto.AccountDto;

public class AccountRep {
  private String username;
  private String email;
  private String fullName;
  private String password;
  private boolean loggedIn;

  public static AccountDto toAccountDto(AccountRep accountRep) {
    AccountDto account = new AccountDto();

    account.setUsername(accountRep.getUsername());
    account.setEmail(accountRep.getEmail());
    account.setFullName(accountRep.getFullName());
    account.setLoggedIn(accountRep.isLoggedIn());
    account.setPassword(accountRep.getPassword());
    return account;
  }

  public static AccountRep fromAccountDto(AccountDto accountDto) {
    AccountRep account = new AccountRep();
    account.setUsername(accountDto.getUsername());
    account.setEmail(accountDto.getEmail());
    account.setFullName(accountDto.getFullName());
    account.setLoggedIn(accountDto.isLoggedIn());
    account.setPassword(accountDto.getPassword());
    return account;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getUsername() {
    return this.username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return this.email;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public boolean isLoggedIn() {
    return this.loggedIn;
  }

  public void setLoggedIn(boolean status) {
    this.loggedIn = status;
  }

}
