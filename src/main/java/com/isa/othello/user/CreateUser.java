
package com.isa.othello.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

/**
 *New user registration class.
 * @author Bhavana
 */
@ManagedBean
@RequestScoped
public class CreateUser {
    @ManagedProperty(value="")
    private String name;
    
    @ManagedProperty(value="")
    private String password;
    
    @ManagedProperty(value="#{user}")
    private User localUser;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLocalUser() {
        return localUser;
    }

    public void setLocalUser(User localUser) {
        this.localUser = localUser;
    }
    
    public String create() throws NoSuchAlgorithmException, InvalidKeySpecException, ClassNotFoundException, SQLException
    {
        UserService.create(name,password);
        //localUser.setName(name);
        //localUser.setPassword(password);
        //localUser.login();
        return "index";
    }
}
