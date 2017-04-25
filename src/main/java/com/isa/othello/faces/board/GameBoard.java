
package com.isa.othello.faces.board;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;

/**
 *Contains the game board information.
 * @author Manish
 */
@FacesComponent(value="com.isa.othello.faces.board.GameBoard")
public class GameBoard extends UIInput implements ClientBehaviorHolder{
    
    public static final String MY_COMPONENT_TYPE 
            = "com.isa.othello.faces.board.GameBoard";

    @Override
    public String getFamily() {
       return "com.isa.gameboard";
    }
    
    enum PropertyKeys {gameIdProperty, disabled};

    @Override
    public void decode(FacesContext context) {
        super.decode(context);
    }
    
    public String getGameIdProperty()
    {
        return (String)getStateHelper().eval(PropertyKeys.gameIdProperty);
    }
    
    public void setGameIdProperty(String gameid)
    {
        getStateHelper().put(PropertyKeys.gameIdProperty, gameid);
    }
    
    public boolean isDisabled()
    {
        return (Boolean)getStateHelper().eval(PropertyKeys.disabled);
    }
    
    public void setDisabled(boolean gameid)
    {
        getStateHelper().put(PropertyKeys.disabled, gameid);
    }
    
    
    
    
}
