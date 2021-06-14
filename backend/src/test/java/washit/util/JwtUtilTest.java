package washit.util;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilTest {

  private static final String secret = "wksPWxvOlywsgDw3t2E1Afwqw1TbJKcfuv33dhYliLc=";
  private static final String badSecret = "wksPWxvOlywsgDw3t2F1Afwqw1TbJKcfuv33dhYliLc=";

  @Test
  void generateAndValidateJWT() {
    JwtUtil jwtUtil = new JwtUtil(secret);
    String username = "testuser";
    String jwt = jwtUtil.generateToken(username);
    Optional<String> maybeUsername = jwtUtil.getUsernameFromToken(jwt);
    assertTrue(maybeUsername.isPresent());
    assertEquals(username, maybeUsername.get());
  }

  @Test
  void generateAndValidateBadJWT() {
    JwtUtil jwtUtilWithGoodSecret = new JwtUtil(secret);
    JwtUtil jwtUtilWithBadSecret = new JwtUtil(badSecret);
    String username = "testuser";
    String badJWT = jwtUtilWithBadSecret.generateToken(username);
    Optional<String> maybeUsername = jwtUtilWithGoodSecret.getUsernameFromToken(badJWT);
    assertTrue(maybeUsername.isEmpty());
  }

}