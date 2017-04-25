package com.isa.othello.user;

import com.isa.othello.user.dao.UserDAO;
import com.isa.othello.user.dao.UserDAOService;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Random;
import java.util.UUID;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *A utility class for USER.
 * @author Bhavana
 */
@ApplicationScoped
public class UserService {

    public static final String COOKIE_UUID = "uuid";

    public static String getUser(int id) {
        if (id <= 0) {
            return "";
        }
        return UserDAOService.getInstance().findUser(id).getName();
    }

    public static int putUser(String user, String password) {
        UserDAO dao = new UserDAO();
        dao.setName(user);
        dao.setPwd(password);
        return UserDAOService.getInstance().persist(dao);

    }

    public static void removeUser(int id) {
        UserDAOService.getInstance().remove(id);
    }

    public static int logIn(String user, String password) {
        UserDAO dao = UserDAOService.getInstance().findUser(user);
        if (dao == null) {
            return -1;
        }
        UUID uuid = UUID.randomUUID();
        HttpServletResponse httpServletResponse =
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie(COOKIE_UUID, uuid.toString());   //TODO encrypt
        cookie.setMaxAge(21 * 24 * 60 * 60); //Sets the maximum age in seconds for this Cookie.
        cookie.setComment("LoggedInUser");
        cookie.setPath("/"); //for all Applications
        httpServletResponse.addCookie(cookie);


        dao.setUuid(uuid.toString());
        UserDAOService.getInstance().merge(dao);
        return dao.getId();

    }

    /**
     * Checks if the user is logged in via (1) cookie (2) tomcat container.
     *
     * @return -1 if cookie is not set or cookie value is not recognised as
     * login
     */
    public static int isLoggedIn() {
        Cookie[] cookies = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getCookies();
        if (cookies == null || cookies.length == 0) {
            return -1; //not Logged In
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(COOKIE_UUID) && cookie.getValue() != null) {
                final UserDAO findUserByUUID = UserDAOService.getInstance().findUserByUUID(cookie.getValue());
                if (findUserByUUID == null) {
                    HttpServletResponse httpServletResponse =
                            (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                    Cookie invalidate = new Cookie(COOKIE_UUID, "");
                    invalidate.setMaxAge(0);
                    invalidate.setComment("LoggedInUser");
                    invalidate.setPath("/");
                    httpServletResponse.addCookie(invalidate);
                    return -1;
                }
                HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                try {
                    r.login(findUserByUUID.getName(), findUserByUUID.getPwd());
                } catch (ServletException ex) {
                    LoggerFactory.getLogger(User.class.getName()).error(ex.toString(), ex);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.toString(), ""));
                    return -1;
                }
                return findUserByUUID.getId();
            }
        }
        return -1;
    }

    public static void logout(int id) {

        UserDAO dao = UserDAOService.getInstance().findUser(id);
        dao.setUuid(null);
        UserDAOService.getInstance().merge(dao);

        HttpServletResponse httpServletResponse =
                (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        Cookie cookie = new Cookie(COOKIE_UUID, "");
        cookie.setMaxAge(0);
        cookie.setComment("LoggedInUser");
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    static void create(String name, String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String saltofpwd = "";
        final int IT = 1000;
        final int LENGTH = 192;

        final Random r = new SecureRandom();

        byte[] salt = new byte[32];

        r.nextBytes(salt);

        saltofpwd = String.format("%x", new BigInteger(salt));

        char[] passwordChars = password.toCharArray();

        PBEKeySpec spec = new PBEKeySpec(
                passwordChars, saltofpwd.getBytes(), IT, LENGTH);
        SecretKeyFactory key;

        key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hashedPassword = key.generateSecret(spec).getEncoded();

        password = String.format("%x", new BigInteger(hashedPassword));

        UserDAOService.getInstance().create(name, password, saltofpwd);

    }

    static boolean exists(String name) {
        return UserDAOService.getInstance().findUser(name) != null;
    }
}
