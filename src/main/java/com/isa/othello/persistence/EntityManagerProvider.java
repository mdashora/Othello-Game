
package com.isa.othello.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *EntityManagerProvider is a utility class to access the database.
 *Used to create and remove persistent entity instances.
 * @author Bhavana
 */
public class EntityManagerProvider implements ServletContextListener{
    
    private static EntityManagerFactory fac;
    public static EntityManager getEntityManager()
    {
        try
        {
            return fac.createEntityManager();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("java:/othelloEM not found", e);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        fac = Persistence.createEntityManagerFactory("othelloEM");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(fac != null)
        {
            fac.close();
        }
    }
    
}
