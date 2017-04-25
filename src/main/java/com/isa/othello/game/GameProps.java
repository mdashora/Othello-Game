
package com.isa.othello.game;

/**
 *Utility class for game.
 * @author Manish
 */
public class GameProps {
    
    int playeridBlack;
    int playeridWhite;

    public int getPlayeridBlack() {
        return playeridBlack;
    }

    public void setPlayeridBlack(int playeridBlack) {
        this.playeridBlack = playeridBlack;
    }

    public int getPlayeridWhite() {
        return playeridWhite;
    }

    public void setPlayeridWhite(int playeridWhite) {
        this.playeridWhite = playeridWhite;
    }

    @Override
    public String toString() {
        return "GameProps{" + "playeridBlack=" + playeridBlack + ", playeridWhite=" + playeridWhite + '}';
    }   
}
