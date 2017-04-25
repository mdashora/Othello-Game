
package com.isa.othello.game;

import com.isa.othello.user.User;
import com.isa.othello.user.UserService;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Game class manages all whole game and related statistics.
 * @author Manish
 */
@ManagedBean
@ViewScoped
public class Game implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(Game.class);
    @ManagedProperty(value = "0")
    private int id;
    @ManagedProperty(value = "start")
    private String move;
    @ManagedProperty(value = "#{user}")
    private User localUser;
    @ManagedProperty(value = "#{boardService}")
    private BoardService boardService;
    int gamesWonBlack = 0;
    int gamesLostBlack = 0;
    int gamesWonWhite = 0;
    int gamesLostWhite = 0;
    boolean statValueBlack = false;

    public boolean isStatValueBlack() {
        return statValueBlack;
    }

    public void setStatValueBlack(boolean statValueBlack) {
        this.statValueBlack = statValueBlack;
    }

    public boolean isStatValueWhite() {
        return statValueWhite;
    }

    public void setStatValueWhite(boolean statValueWhite) {
        this.statValueWhite = statValueWhite;
    }
    boolean statValueWhite = false;

    public int getGamesWonBlack() {
        return gamesWonBlack;
    }

    public void setGamesWonBlack(int gamesWonBlack) {
        this.gamesWonBlack = gamesWonBlack;
    }

    public int getGamesLostBlack() {
        return gamesLostBlack;
    }

    public void setGamesLostBlack(int gamesLostBlack) {
        this.gamesLostBlack = gamesLostBlack;
    }

    public int getGamesWonWhite() {
        return gamesWonWhite;
    }

    public void setGamesWonWhite(int gamesWonWhite) {
        this.gamesWonWhite = gamesWonWhite;
    }

    public int getGamesLostWhite() {
        return gamesLostWhite;
    }

    public void setGamesLostWhite(int gamesLostWhite) {
        this.gamesLostWhite = gamesLostWhite;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String nextMove) {
        this.move = nextMove;
    }

    public String move(String row, String column) {
        try {
            move = row + column;
            makeMove();
            FacesContext.getCurrentInstance().getExternalContext().redirect("game.xhtml?gameid=" + id);
            return "game";
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    private void makeMove() {
        try {
            Board board = boardService.getBoard(id);
            if (board == null) {
                board = new Board();
                board.markNextMoves();
            }

            board.makeMove(move);

            id = boardService.save(id, board);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), null));
        }
    }

    public int newGame() {
        return boardService.newGame();
    }

    public String reset() {
        Board board = boardService.getBoard(id);
        board.reset();
        boardService.save(id, board);
        return "game";
    }

    public Board getBoard() {
        if (id == 0) {
            throw new IllegalStateException("GameID is NULL and a board is retrieved.");
        }
        Board board = boardService.getBoard(id);
        if (board == null) {
            board = new Board();
            board.markNextMoves();
            boardService.save(id, board);

        }
        return board;
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", move=" + move + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int pId) {
        this.id = pId;
    }

    public String getBlackPlayerName() {

        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("black", UserService.getUser(getBlackPlayerId()));

        return UserService.getUser(getBlackPlayerId());
    }

    public String getWhitePlayerName() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getExternalContext().getSessionMap().put("white", UserService.getUser(getWhitePlayerId()));
        return UserService.getUser(getWhitePlayerId());
    }

    public int getBlackPlayerId() {
        return GamePropsService.get(id).getPlayeridBlack();
    }

    public int getWhitePlayerId() {
        return GamePropsService.get(id).getPlayeridWhite();
    }

    public void playBlack() throws ClassNotFoundException, SQLException {
        GameProps props = GamePropsService.get(id);
        props.setPlayeridBlack(localUser.getId());
        GamePropsService.save(id, props);


    }

    public void statBlack() throws ClassNotFoundException, SQLException {

        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/othello",
                "othello", "othello");
        PreparedStatement st = null;
        ResultSet rs = null;

        st = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
        st.setString(1, getBlackPlayerName());
        rs = st.executeQuery();
        while (rs.next()) {


            gamesWonBlack = rs.getInt(3);
            gamesLostBlack = rs.getInt(4);


        }
        statValueBlack = true;

    }

    public void statWhite() throws SQLException, ClassNotFoundException {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/othello",
                "othello", "othello");
        PreparedStatement st = null;
        ResultSet rs = null;

        st = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
        st.setString(1, getWhitePlayerName());
        rs = st.executeQuery();
        while (rs.next()) {


            gamesWonWhite = rs.getInt(3);
            gamesLostWhite = rs.getInt(4);


        }
        statValueWhite = true;
    }

    public void playWhite() throws ClassNotFoundException, SQLException {
        GameProps props = GamePropsService.get(id);
        props.setPlayeridWhite(localUser.getId());
        GamePropsService.save(id, props);
    }

    public User getLocalUser() {
        return localUser;
    }

    public void setLocalUser(User localUser) {
        this.localUser = localUser;
    }

    public boolean isUsersTurn() {
        if (getBoard().getNextPlayerBlack() && localUser.getId() == GamePropsService.get(id).getPlayeridBlack()) {
            return true;
        } else if (!getBoard().getNextPlayerBlack() && localUser.getId() == GamePropsService.get(id).getPlayeridWhite()) {
            return true;
        }

        return false;
    }

    public boolean isHasBlackPlayer() {
        return getBlackPlayerId() > 0;
    }

    public boolean isHasWhitePlayer() {
        return getWhitePlayerId() > 0;
    }

    public boolean isUserIsPlayer() {
        return getBlackPlayerId() == localUser.getId() || getWhitePlayerId() == localUser.getId();
    }

    public String getState(String row, String column) {
        return getBoard().getState(row + column).toString();
    }

    public boolean isFieldDisabled(String row, String column) {
        if (!isUsersTurn()) {
            return true;
        } else {
            return false;
        }
    }

    public BoardService getBoardService() {
        return boardService;
    }

    public void setBoardService(BoardService boardService) {
        this.boardService = boardService;
    }
}
