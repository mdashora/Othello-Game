
package com.isa.othello.game;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

/**
 *Utility class for game Creation.
 * @author Manish
 */
@ManagedBean
@ApplicationScoped
public class Games implements Serializable{

    @ManagedProperty(value="#{boardService}")
    private BoardService boardService ;
    
    /** Creates a new instance of Games */
    public Games() 
    {
        super();
    }
    
    public int newGame()
    {
       return boardService.newGame();     
    }
    
    public List<Integer> getAllBoards()
    {
        final ArrayList list = new ArrayList(boardService.getAllGames());
        Collections.sort(list);
        return list;
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }
}
