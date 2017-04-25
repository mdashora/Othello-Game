
package com.isa.othello.user.dao;

import com.isa.othello.persistence.EntityManagerProvider;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

/**
 *Utiltiy class for user DAO. Creates the new user..
 * @author Bhavana
 */
@ManagedBean
@RequestScoped
public class UserDAOService
{
    private static final String UUID_QUERY = "SELECT u FROM users u WHERE u.uuid = :uuid";
    private static final String NAME_QUERY = "SELECT u FROM users u WHERE u.name = :name";
    
    private EntityManager em = EntityManagerProvider.getEntityManager();
    
    private UserDAOService(){};
    
    public UserDAO findUserByUUID(String uuid)
    {
        Query q = em.createQuery(UUID_QUERY);
        q.setParameter("uuid", uuid);
        List<?> l = q.getResultList(); //throws no Exception if entity is not found
        if(l == null || l.isEmpty())
        {
            return null;
        }
        return (UserDAO) l.get(0); //return first HIT
    }
    
    public UserDAO findUser(String name)
    {
        Query q = em.createQuery(NAME_QUERY);
        q.setParameter("name", name);
        List<?> l = q.getResultList(); //throws no Exception if entity is not found
        if(l == null || l.isEmpty())
        {
            return null;
        }
        return (UserDAO) l.get(0); //return first HIT
    }
    
    public UserDAO findUser(int id)
    {
        return em.find(UserDAO.class, id);
    }
    
    public static UserDAOService getInstance()
    {
        return new UserDAOService();
    }

    public int persist(UserDAO dao) {
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        em.persist(dao); //persist the entity
        em.flush();
        tr.commit();
        return findUser(dao.getName()).getId(); //reload and return id
    }

    public void remove(int id) {
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        em.remove(findUser(id));
        em.flush();
        tr.commit();
    }

    public void create(String name, String password, String salt) {
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        UserDAO dao = new UserDAO();
        dao.setName(name);
        dao.setPwd(password);
        dao.setSalt(salt);
        em.persist(dao);
        RoleDAO role = new RoleDAO();
        role.setRole("othello"); //standard Role for every new User
        role.setName(name);
        dao.getRoles().add(role);
        tr.commit();
    }

    public void merge(UserDAO dao) {
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        em.merge(dao);
        tr.commit();
    }
}
