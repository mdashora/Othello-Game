
package com.isa.othello.user.dao;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *DAO class for Roles table
 * @author Bhavana
 */
@Entity(name="roles")
@Table(name="roles")
public class RoleDAO implements Serializable
{
    @Id   
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="rolesSeq")
    private int id;
    
    @Column(name="name")
    private String name;
    
    @Column(name="userrole")
    private String role;
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } 

    @Override
    public String toString() {
        return "RoleDAO{name=" + name + ", role=" + role + '}';
    }
    
    
}
