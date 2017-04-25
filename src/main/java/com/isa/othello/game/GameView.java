
package com.isa.othello.game;

import java.io.Serializable;


/**
 *A POJO with getter and setters for Game Board.
 * @author Manish
 */
public class GameView implements Serializable {
    
    private final Board board;
    
    protected GameView(Board pBoard) {
        board = pBoard;
    }

    public String getA1() {
        return board.getState("a1").toString();
    }

    public String getA2() {
        return board.getState("a2").toString();
    }

    public String getA3() {
        return board.getState("a3").toString();
    }

    public String getA4() {
        return board.getState("a4").toString();
    }

    public String getA5() {
        return board.getState("a5").toString();
    }

    public String getA6() {
        return board.getState("a6").toString();
    }

    public String getA7() {
        return board.getState("a7").toString();
    }

    public String getA8() {
        return board.getState("a8").toString();
    }

    public String getB1() {
        return board.getState("b1").toString();
    }

    public String getB2() {
        return board.getState("b2").toString();
    }

    public String getB3() {
        return board.getState("b3").toString();
    }

    public String getB4() {
        return board.getState("b4").toString();
    }

    public String getB5() {
        return board.getState("b5").toString();
    }

    public String getB6() {
        return board.getState("b6").toString();
    }

    public String getB7() {
        return board.getState("b7").toString();
    }

    public String getB8() {
        return board.getState("b8").toString();
    }

    public String getC1() {
        return board.getState("c1").toString();
    }

    public String getC2() {
        return board.getState("c2").toString();
    }

    public String getC3() {
        return board.getState("c3").toString();
    }

    public String getC4() {
        return board.getState("c4").toString();
    }

    public String getC5() {
        return board.getState("c5").toString();
    }

    public String getC6() {
        return board.getState("c6").toString();
    }

    public String getC7() {
        return board.getState("c7").toString();
    }

    public String getC8() {
        return board.getState("c8").toString();
    }

    public String getD1() {
        return board.getState("d1").toString();
    }

    public String getD2() {
        return board.getState("d2").toString();
    }

    public String getD3() {
        return board.getState("d3").toString();
    }

    public String getD4() {
        return board.getState("d4").toString();
    }

    public String getD5() {
        return board.getState("d5").toString();
    }

    public String getD6() {
        return board.getState("d6").toString();
    }

    public String getD7() {
        return board.getState("d7").toString();
    }

    public String getD8() {
        return board.getState("d8").toString();
    }

    public String getE1() {
        return board.getState("e1").toString();
    }

    public String getE2() {
        return board.getState("e2").toString();
    }

    public String getE3() {
        return board.getState("e3").toString();
    }

    public String getE4() {
        return board.getState("e4").toString();
    }

    public String getE5() {
        return board.getState("e5").toString();
    }

    public String getE6() {
        return board.getState("e6").toString();
    }

    public String getE7() {
        return board.getState("e7").toString();
    }

    public String getE8() {
        return board.getState("e8").toString();
    }

    public String getF1() {
        return board.getState("f1").toString();
    }

    public String getF2() {
        return board.getState("f2").toString();
    }

    public String getF3() {
        return board.getState("f3").toString();
    }

    public String getF4() {
        return board.getState("f4").toString();
    }

    public String getF5() {
        return board.getState("f5").toString();
    }

    public String getF6() {
        return board.getState("f6").toString();
    }

    public String getF7() {
        return board.getState("f7").toString();
    }

    public String getF8() {
        return board.getState("f8").toString();
    }

    public String getG1() {
        return board.getState("g1").toString();
    }

    public String getG2() {
        return board.getState("g2").toString();
    }

    public String getG3() {
        return board.getState("g3").toString();
    }

    public String getG4() {
        return board.getState("g4").toString();
    }

    public String getG5() {
        return board.getState("g5").toString();
    }

    public String getG6() {
        return board.getState("g6").toString();
    }

    public String getG7() {
        return board.getState("g7").toString();
    }

    public String getG8() {
        return board.getState("g8").toString();
    }

    public String getH1() {
        return board.getState("h1").toString();
    }

    public String getH2() {
        return board.getState("h2").toString();
    }

    public String getH3() {
        return board.getState("h3").toString();
    }

    public String getH4() {
        return board.getState("h4").toString();
    }

    public String getH5() {
        return board.getState("h5").toString();
    }

    public String getH6() {
        return board.getState("h6").toString();
    }

    public String getH7() {
        return board.getState("h7").toString();
    }

    public String getH8() {
        return board.getState("h8").toString();
    }
}
