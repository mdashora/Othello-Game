package com.isa.othello.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;

/**
 *User class have functionality of user login and logout.
 * @author Bhavana
 */
@ManagedBean
@SessionScoped
public class User implements Serializable {

    public static final String USER_ROLE_OTHELLO = "othello";
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(User.class);

    /**
     * Creates a new instance of User
     */
    public User() {
    }
    @ManagedProperty(value = "0") //0 --> check the cookie, only once per Session
    private int id;
    @ManagedProperty(value = "")
    private String name;
    @ManagedProperty(value = "")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoggedIn() {
        if (id > 0) {
            return true;
        } else if (id == -1) {
            return false;
        }

        // == 0, means check
        id = UserService.isLoggedIn();
        if (id > 0) {
            name = UserService.getUser(id);
        }
        return id > 0;
    }

    public String logOut() {
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            r.logout();
        } catch (ServletException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, ex.toString(), ""));
        }
        UserService.logout(id);
        name = "";
        id = 0;
        return "index?faces-redirect=true";
    }

    public boolean exists() {
        return UserService.exists(name);
    }

    public boolean notExists() {
        return !UserService.exists(name);
    }

    public boolean isCanPlay() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).isUserInRole(USER_ROLE_OTHELLO);
    }

    public String login() throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/othello",
                    "othello", "othello");
            PreparedStatement st = null;
            ResultSet rs = null;

            st = con.prepareStatement("SELECT ID, NAME, PWD, UUID, SALT FROM USERS where name = ?");

            st.setString(1, name);

            rs = st.executeQuery();
            if (rs.next()) {

                int rowCount = rs.getInt(1);

                if (rowCount > 0) {

                    final int IT = 1000;
                    final int LENGTH = 192;

                    char[] passwordChars = password.toCharArray();
                    byte[] saltBytes = rs.getString(5).getBytes();

                    PBEKeySpec spec = new PBEKeySpec(
                            passwordChars, saltBytes, IT, LENGTH);
                    SecretKeyFactory key;

                    key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                    byte[] hashedPassword = key.generateSecret(spec).getEncoded();
                    password = String.format("%x", new BigInteger(hashedPassword));

                }
                r.login(name, password);
                if (((name != null) && (name.length() > 0)) && (password.equals(rs.getString(3)))) {
                    id = UserService.logIn(name, password);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Username or Password", ""));
                    return "login";
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Username or Password", ""));
                return "login";
            }

        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Username or Password", ""));
            return "login";
        }
        return "/index.xhtml?faces-redirect=true";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", password=" + password + '}';
    }
}
