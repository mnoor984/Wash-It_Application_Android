package washit.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Optional;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  public JwtUtil() {
  }

  public JwtUtil(String secret) {
    this.secret = secret;
  }

  /**
   * @param token token to get the username from
   * @return an Optional with a username inside if the token is valid. An empty Optional if
   * the token is invalid.
   */
  public Optional<String> getUsernameFromToken(String token) {
    Jws<Claims> claims;
    try {
      claims = Jwts.parserBuilder()
                   .setSigningKey(secret)
                   .build()
                   .parseClaimsJws(token);
    } catch (SignatureException e) {
      return Optional.empty();
    }
    return Optional.of(claims.getBody()
                             .getSubject());
  }

  /**
   * @param username username to sign
   * @return the signed token
   */
  public String generateToken(String username) {
    final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    return Jwts.builder()
               .setSubject(username)
               .signWith(key)
               .compact();
  }

}
