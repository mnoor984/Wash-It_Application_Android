package washit.service;

import org.springframework.stereotype.Service;
import washit.service.exception.AccountException;
import washit.util.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Service
public class RequestAuthenticator {

  private final JwtUtil jwtUtil;

  public RequestAuthenticator(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }


  public String getUsernameFromHeaders(Map<String, String> headers) {
    if (!headers.containsKey("authorization")) {
      throw new AccountException("Authorization header required.");
    }
    String authorizationHeader = headers.get("authorization");
    if (!authorizationHeader.contains("Bearer")) {
      throw new AccountException("Authorization header must use Bearer schema \"Bearer <TOKEN>\"");
    }
    String token = authorizationHeader.replace("Bearer", "").trim();
    final Optional<String> maybeUsername = jwtUtil.getUsernameFromToken(token);
    if (maybeUsername.isEmpty()) {
      throw new AccountException("Authorization token is invalid.");
    }
    return maybeUsername.get();
  }
}
