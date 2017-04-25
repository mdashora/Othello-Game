
package com.isa.othello.game;

import java.io.Serializable;
import java.util.TreeMap;
import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;

/**
 *Utility class for GameProps class.
 * @author Manish
 */
@ApplicationScoped
@ManagedBean
public class GamePropsService implements Serializable {
    private static final TreeMap<Integer, GameProps> props = new TreeMap<Integer, GameProps>();
    
    public static void save(int id, GameProps gameProps)
    {
        props.put(id, gameProps);
    }
    
    public static GameProps get(int id)
    {
        GameProps get = props.get(id);
        if(get == null)
        {
            get = new GameProps();
            save(id, get);
        }
        return get;
    }
}
