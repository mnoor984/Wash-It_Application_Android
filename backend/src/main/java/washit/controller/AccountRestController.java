package washit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import washit.controller.representation.AccountRep;
import washit.dto.AccountDto;
import washit.entity.Account;
import washit.service.AccountService;
import washit.service.RequestAuthenticator;
import washit.service.exception.AccountException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class AccountRestController {

  private final AccountService service;
  private final RequestAuthenticator authenticator;

  public AccountRestController(AccountService service, RequestAuthenticator authenticator) {
    this.service = service;
    this.authenticator = authenticator;
  }

  @GetMapping(value = {"/accounts", "/accounts/"})
  public ResponseEntity<List<AccountDto>> findAllAccounts() {
    List<AccountDto> allAccDto = service.getAllAccounts()
                                        .stream()
                                        .map(this::convertToDto)
                                        .collect(Collectors.toList());
    return new ResponseEntity<>(allAccDto, HttpStatus.OK);
  }

  @GetMapping(value = {"/accounts/{username}", "/accounts/{username}/"})
  public ResponseEntity<AccountDto> findAccount(@PathVariable("username") String username) {
    Account acc = service.findAccountByUsername(username);
    return new ResponseEntity<>(convertToDto(acc), HttpStatus.OK);
  }

  @PostMapping(value = {"/accounts", "/accounts/"})
  public ResponseEntity<AccountDto> createAccount(@RequestBody AccountDto dto) {
    Account acc = service.createAccount(dto);
    return new ResponseEntity<>(convertToDto(acc), HttpStatus.CREATED);
  }

  @PutMapping(value = {"/accounts/{username}"})
  public ResponseEntity<AccountDto> updateAccount(
    @PathVariable("username") String username,
    @RequestBody AccountDto dto,
    @RequestHeader Map<String, String> headers) throws AccountException {

    // authorization
    String tokenUsername = authenticator.getUsernameFromHeaders(headers);
    if (!username.equals(tokenUsername))
      throw new AccountException("Cannot update an account you're not logged into");

    Account acc = service.updateAccount(username, dto);
    return new ResponseEntity<>(convertToDto(acc), HttpStatus.OK);
  }

  @DeleteMapping(value = {"/accounts/{username}/{confirm}"})
  public ResponseEntity<String> deleteAccount(@PathVariable("username") String username,
                                              @PathVariable("confirm") boolean confirm,
                                              @RequestHeader Map<String, String> headers) throws AccountException {
    // authorization
    String tokenUsername = authenticator.getUsernameFromHeaders(headers);
    if (!username.equals(tokenUsername))
      throw new AccountException("Cannot delete an account you're not logged into");

    if (!confirm) {
      return new ResponseEntity<>("Account deletion cancelled", HttpStatus.OK);
    }
    service.deleteAccountByUsername(username);
    return new ResponseEntity<>("Account successfully deleted", HttpStatus.OK);
  }

  @PostMapping("/login")
  public String login(@RequestBody AccountRep loginInfo) {
    return service.loginToAccount(AccountRep.toAccountDto(loginInfo));
  }

  private AccountDto convertToDto(Account acc) {

    AccountDto accDto = new AccountDto();
    accDto.setUsername(acc.getUsername());
    accDto.setFullName(acc.getFullName());
    accDto.setEmail(acc.getEmail());
    accDto.setPassword(acc.getPassword());
    accDto.setLoggedIn(acc.getLoggedIn());

    return accDto;
  }
}
