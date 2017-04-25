
package com.isa.othello.user.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *DAO class for User table.
 * @author Bhavana
 */
@Entity(name="users")
@Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="name"))
public class UserDAO implements Serializable { 
    
    @Column(name="name", unique=true)
    private String name;
    
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="usersSeq")
    @Id
    private int id;
    
    @Column(name="uuid")
    private String uuid;
    
    @Column(name="pwd")
    private String pwd;
    
    @Column(name="salt")
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    
    @OneToMany(fetch= FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="userid")
    private List<RoleDAO> roles = new ArrayList<RoleDAO>(1);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RoleDAO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDAO> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDAO{" + "name=" + name + ", id=" + id + ", uuid=" + uuid + ", pwd=" + pwd + ", roles=" + roles + '}';
    }

}
