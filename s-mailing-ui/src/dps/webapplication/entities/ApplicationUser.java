package dps.webapplication.entities;

import dps.authentication.AuthenticableUser;
import dps.authentication.utils.PasswordAuthentication;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "ApplicationUser.getAll",
                query = "SELECT m FROM ApplicationUser m"
        ),
        @NamedQuery(
                name = "ApplicationUser.getById",
                query = "SELECT m FROM ApplicationUser m WHERE m.id = :id"
        ),
        @NamedQuery(name = "ApplicationUser.getByUsername", query = "SELECT m FROM ApplicationUser m WHERE m.username = :username"),
        @NamedQuery(name = "ApplicationUser.count", query="SELECT COUNT(m) FROM ApplicationUser m"),
})
public class ApplicationUser implements Serializable, AuthenticableUser {

    private static final long serialVersionUID = 6507165003243550180L;

    private static PasswordAuthentication authentication = new PasswordAuthentication();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    private Integer accessLevel = 0;

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
        if (password.isEmpty()) this.passwordHash = null;
        else this.passwordHash = this.hash(password);
    }

    @XmlTransient
    @JsonbTransient
    public String getPasswordHash() {
        return passwordHash;
    }

    @XmlTransient
    @JsonbTransient
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Integer getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel) {
        this.accessLevel = accessLevel;
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
    @XmlTransient
    @JsonbTransient
    public Boolean isAuthorized(String operation) {
        if ("admin".equals(operation)) {
            return (accessLevel & 1) == 1;
        } else if ("trusted".equals(operation)) {
            return ((accessLevel & 2) >> 1) == 1;
        } else if ("super".equals(operation)) {
            return ((accessLevel & 4) >> 2) == 1;
        } else {
            return true;
        }
    }

    private static String hash(String password)
    {
        return authentication.hash(password.toCharArray());
    }

    private static boolean check(String password, String hash)
    {
        return authentication.authenticate(password.toCharArray(),hash);
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
