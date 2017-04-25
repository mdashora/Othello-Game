
package com.isa.othello.game;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 * Utility class for Board.
 * @author Manish
 */
@ApplicationScoped
@ManagedBean
public class BoardService implements Serializable {

    private final TreeMap<Integer, Board> boards = new TreeMap<Integer, Board>();
    private final AtomicInteger count = new AtomicInteger(0);
    
    public BoardService()
    {
        super();
    }
    
    public Board getBoard(int id) {
        return boards.get(id);
    }

    public int save(int pId, Board board) {
        if(boards.get(pId) != null)
        {
            boards.put(pId, board);
            return pId;
        }
        else
        {
            int id = count.incrementAndGet();
            boards.put(id, board);
            return id;
        }
    }
    
    public Collection<Integer> getAllGames()
    {
        return Collections.unmodifiableCollection(boards.keySet());
    }

    public int newGame() {
         int id = count.incrementAndGet();
            final Board board = new Board();
            board.markNextMoves();
            boards.put(id, board);
            return id;
    }
    
}
