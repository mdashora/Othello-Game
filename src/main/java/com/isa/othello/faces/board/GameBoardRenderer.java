
package com.isa.othello.faces.board;

import com.isa.othello.game.Board;
import com.isa.othello.game.Game;
import java.io.IOException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *GameBoardRenderer renders the game based on user availability.
 * @author Manish
 */
@FacesRenderer(componentFamily= "com.isa.gameboard", rendererType= "com.isa.othello.faces.board.GameBoard")
public class GameBoardRenderer extends Renderer{

    private static final Logger logger = LoggerFactory.getLogger(GameBoardRenderer.class);
    
    public String[] rows = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
 
    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter w = context.getResponseWriter();
        w.endElement("table");
        w.flush();
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {      
        ResponseWriter w = context.getResponseWriter();
        w.startElement("table", component);
        w.writeAttribute("id", component.getClientId(context),"id");
        w.writeAttribute("name", component.getClientId(context),"clientId");
        w.writeAttribute("class", "gameboard",null);
    }
    
    

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        ResponseWriter w = context.getResponseWriter();
        final Game gameBean = (Game) context.getApplication().evaluateExpressionGet(context, "#{game}", Game.class);
        Board game = gameBean.getBoard();
        GameBoard gameBoard = (GameBoard) component;
        
        for(String row : rows)
        {
            w.startElement("tr", component);
            for(int i = 1; i < 9; i++)
            {
                Board.STATE state = game.getState(row + i);
                w.startElement("td", component);
                w.startElement("form", component);
                //w.writeAttribute("action", "game?faces-redirect=true", "");
                w.writeAttribute("method", "POST", null); 
                w.startElement("input", component);
                w.writeAttribute("type", "hidden", null);
                w.writeAttribute("name", "nextmove", null);
                w.writeAttribute("value", row + i, "nextmove"); 
                w.endElement("input");
                w.startElement("input", component);
                w.writeAttribute("type", "hidden", null);
                w.writeAttribute("name",((GameBoard)component).getGameIdProperty(), null);
                w.writeAttribute("value", gameBean.getId(), ((GameBoard)component).getGameIdProperty()); 
                w.endElement("input");
                w.startElement("input", component);
                
                //when board is disabled: don't show valid moves
                if(gameBoard.isDisabled() && (state == Board.STATE.SELECTABLE_BLACK || state == Board.STATE.SELECTABLE_WHITE))
                {
                    state = Board.STATE.NOT_SELECTABLE;            
                }
                
                w.writeAttribute("class", state.toString(), null); //write the state, Behaviour will be defined by css
                
                if(!(state == Board.STATE.SELECTABLE_BLACK || state == Board.STATE.SELECTABLE_WHITE))
                {
                    w.writeAttribute("disabled", "disabled", null); 
                }
                w.writeAttribute("type", "submit", row);
                w.writeAttribute("value", "", row);
                w.endElement("input");
                w.endElement("form");
                w.endElement("td");
            }
            w.endElement("tr");
        }
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
    
    
}
