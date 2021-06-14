package washit.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account implements Serializable {

  private static final long serialVersionUID = -7484956675718921268L;

  @Id
  @Column(name = "username")
  private String username;

  @Column(name = "email")
  private String email;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "password")
  private String password;

  @Column(name = "is_logged_in")
  private Boolean isLoggedIn;

  @OneToMany(cascade = CascadeType.REMOVE,
    mappedBy = "creator"
  )
  private final List<Bid> bids = new ArrayList<>(); // DO NOT provide getters, setters, or add to constructor

  public Account() {
  }

  public Account(String username, String email, String fullName, String password, Boolean isLoggedIn) {
    this.username = username;
    this.email = email;
    this.fullName = fullName;
    this.password = password;
    this.isLoggedIn = isLoggedIn;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Boolean getLoggedIn() {
    return isLoggedIn;
  }

  public void setLoggedIn(Boolean loggedIn) {
    isLoggedIn = loggedIn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final Account account = (Account) o;
    return username.equals(account.username) && email.equals(account.email) && fullName.equals(account.fullName) &&
           password.equals(account.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, email, fullName, password);
  }

  @Override
  public String toString() {
    return "User{" +
           "username='" + username + '\'' +
           ", email='" + email + '\'' +
           ", fullName='" + fullName + '\'' +
           ", password='" + password + '\'' +
           '}';
  }
}
