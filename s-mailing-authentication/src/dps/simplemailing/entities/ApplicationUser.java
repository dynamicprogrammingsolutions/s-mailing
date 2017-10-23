package dps.simplemailing.entities;

import dps.authentication.AuthenticableUser;
import dps.simplemailing.authentication.PasswordAuthentication;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(name = "ApplicationUser.getByUsername", query = "SELECT m FROM ApplicationUser m WHERE m.username = :username"),
})
public class ApplicationUser implements Serializable, AuthenticableUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    private String passwordHash;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.passwordHash = this.hash(password);
    }

    @Override
    public Boolean checkCredentials(String username, String password) {
        try {
            if (username.equals(this.username) && check(password, this.passwordHash)) return true;
            else return false;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public Boolean isAuthorized(String operation) {
        return true;
    }

    public static String hash(String password)
    {
        return new PasswordAuthentication().hash(password);
    }

    public static boolean check(String password, String hash)
    {
        return new PasswordAuthentication().authenticate(password,hash);
    }


}
