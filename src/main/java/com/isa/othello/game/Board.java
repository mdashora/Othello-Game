
package com.isa.othello.game;

import com.isa.othello.user.UserService;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *Basic game board structure. All operation regarding game moves are performed.
 * @author Manish
 */
public class Board implements Serializable {

    public static final String STARTING_POSITION = "e5w,d4w,e4b,d5b,";
    int wcount = 0;
    int bcount = 0;
    String playerwon = "";
    String playerlost = "";
    Game player = new Game();

    private boolean isLegalMove(STATE[][] board, int row, int column, boolean black) {

        STATE flip;
        STATE endFlip;
        if (black) {
            flip = STATE.WHITE;
            endFlip = STATE.BLACK;
        } else {
            flip = STATE.BLACK;
            endFlip = STATE.WHITE;
        }

        for (DIRECTION dir : DIRECTION.values()) {
            int nextRow = row + dir.getHor();
            if (nextRow == -1 || nextRow == 8) {
                continue; //at the end of the board
            }
            int nextColumn = column + dir.getVer();
            if (nextColumn == -1 || nextColumn == 8) {
                continue; //at the end of the board
            }
            if (board[nextRow][nextColumn] == flip) {
                boolean flipFound = false;
                //can be flipped, if we can find a beginning i.e. stone of other colour in same direction
                while (true) //can't think of an appropiate recursion end right now
                {
                    nextRow = nextRow + dir.getHor();
                    if (nextRow == -1 || nextRow == 8) {
                        break; //at the end of the board
                    }
                    nextColumn = nextColumn + dir.getVer();
                    if (nextColumn == -1 || nextColumn == 8) {
                        break;
                    }; //at the end of the board

                    //if we find an empty field break;
                    if (board[nextRow][nextColumn] != STATE.BLACK && board[nextRow][nextColumn] != STATE.WHITE) {
                        break;
                    }

                    if (board[nextRow][nextColumn] == endFlip) {
                        flipFound = true;
                        break;
                    }
                }
                log.debug(row + " - " + column + ": " + flipFound);
                if (flipFound) {
                    return true;
                }
            }
        }
        log.debug(row + " - " + column + ": False");
        return false;
    }

    private boolean flip(STATE[][] board, int row, int column, STATE endflip) {
        STATE flip = (endflip == STATE.BLACK) ? STATE.WHITE : STATE.BLACK;

        boolean flipped = false;
        for (DIRECTION dir : DIRECTION.values()) {
            int nextRow = row + dir.getHor();
            if (nextRow == -1 || nextRow == 8) {
                continue; //at the end of the board
            }
            int nextColumn = column + dir.getVer();
            if (nextColumn == -1 || nextColumn == 8) {
                continue; //at the end of the board
            }
            if (board[nextRow][nextColumn] == flip) {
                log.debug("Flip candidate found for " + dir);
                //can be flipped, if we can find a beginning i.e. stone of other colour in same direction
                while (true) //can't think of an appropiate recursion end right now
                {
                    nextRow = nextRow + dir.getHor();
                    if (nextRow == -1 || nextRow == 8) {
                        break; //at the end of the board
                    }
                    nextColumn = nextColumn + dir.getVer();
                    if (nextColumn == -1 || nextColumn == 8) {
                        break; //at the end of the board
                    }
                    //if we find an empty field break;
                    if (board[nextRow][nextColumn] != STATE.BLACK && board[nextRow][nextColumn] != STATE.WHITE) {
                        break;
                    }

                    if (board[nextRow][nextColumn] == endflip) {
                        log.debug("Starting to Flip for " + dir);
                        while (!(row == nextRow && column == nextColumn)) //backwards flipping
                        {
                            nextRow = nextRow - dir.getHor();
                            if (nextRow == -1 || nextRow == 8) {
                                throw new IllegalStateException("Rushed over last move"); //at the end of the board
                            }
                            nextColumn = nextColumn - dir.getVer();
                            if (nextColumn == -1 || nextColumn == 8) {
                                throw new IllegalStateException("Rushed over last move"); //at the end of the board
                            }
                            log.debug("Flipped: " + nextColumn + "/" + nextRow + " to " + endflip);
                            board[nextRow][nextColumn] = endflip;
                            flipped = true;
                        }
                        break;
                    }
                }
            }

            if (!flipped) {
                log.debug(dir + " did not flip");
            }
        }
        return flipped;
    }

    public void markNextMoves() {
        STATE[][] local = transpose(board);
        markNextMoves(local, nextPlayerBlack);
        board = backpose(local);
    }

    private enum DIRECTION {

        N(1, 0), NE(1, 1), E(0, 1), SE(-1, 1), S(-1, 0), SW(-1, -1), W(0, -1), NW(1, -1);
        int hor;
        int ver;

        DIRECTION(int v, int h) {
            ver = v;
            hor = h;

        }

        public int getHor() {
            return hor;
        }

        public void setHor(int hor) {
            this.hor = hor;
        }

        public int getVer() {
            return ver;
        }

        public void setVer(int ver) {
            this.ver = ver;
        }
    }
    private static final Logger log = LoggerFactory.getLogger(Board.class);

    public STATE getState(String string) {
        Pattern p = Pattern.compile(string + "[w|b|o|x]?,");
        Matcher m = p.matcher(board);
        if (m.find()) {
            String state = m.group().substring(2, 3);
            if ("w".equals(state)) {
                return STATE.WHITE;
            } else if ("b".equals(state)) {
                return STATE.BLACK;
            } else if ("o".equals(state)) {
                return STATE.SELECTABLE_WHITE;
            } else if ("x".equals(state)) {
                return STATE.SELECTABLE_BLACK;
            }
        }
        return STATE.NOT_SELECTABLE;
    }

    private STATE[][] transpose(String board) {
        STATE[][] boolBoard = new STATE[8][8];
        Pattern p = Pattern.compile("[a-h][1-8][w|b],");
        Matcher m = p.matcher(board);
        while (m.find()) {
            String sState = m.group().substring(2, 3);
            STATE state;
            if ("b".equals(sState)) {
                state = STATE.BLACK;
            } else {
                state = STATE.WHITE;
            }

            char sRow = m.group().charAt(0);
            int column = Integer.parseInt(m.group().substring(1, 2)) - 1;
            int row = sRow - (int) 'a';
            boolBoard[row][column] = state;
        }

        return boolBoard;
    }

    private String toString(STATE[][] board) {
        StringBuilder build = new StringBuilder("\n");
        for (int i = 0; i < 8; i++) {
            build.append("|");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    char c = (char) (i + 'a');
                    STATE state = board[i][j];
                    if (state == STATE.BLACK) {
                        build.append("b|");
                    } else if (state == STATE.WHITE) {
                        build.append("w|");
                    } else if (state == STATE.SELECTABLE_BLACK) {
                        build.append("x|");
                    } else if (state == STATE.SELECTABLE_WHITE) {
                        build.append("o|");
                    }
                } else {
                    build.append("_|");
                }
            }
            build.append("\n");

        }
        return build.toString();
    }

    private String backpose(STATE[][] board) {
        int black = 0;
        int white = 0;
        StringBuilder build = new StringBuilder("");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    char c = (char) (i + 'a');
                    String pos = "" + c + (j + 1);
                    STATE state = board[i][j];
                    if (state == STATE.BLACK) {
                        black++;
                        build.append(pos).append("b");
                    } else if (state == STATE.WHITE) {
                        white++;
                        build.append(pos).append("w");
                    } else if (state == STATE.SELECTABLE_BLACK) {
                        build.append(pos).append("x");
                    } else if (state == STATE.SELECTABLE_WHITE) {
                        build.append(pos).append("o");
                    }
                    build.append(",");
                }
            }

        }

        blackStones = black;
        whiteStones = white;
        wcount = white;
        bcount = black;
        return build.toString();
    }

    public void makeMove(String nextMove) throws ClassNotFoundException, SQLException {

        if (wcount + bcount < 64) {

            if (nextMove == null
                    || nextMove.length() < 1
                    || !nextMove.matches("[a-h][1-8]")
                    || moves.contains(nextMove)) //starting position or something completely illegal
            {
                markNextMoves();
                return;
            }

            STATE[][] boolboard = transpose(board);
            String lastMove = nextMove;
            int row = nextMove.charAt(0) - (int) 'a';
            int column = Integer.parseInt(nextMove.substring(1, 2)) - 1;





            if (isLegalMove(boolboard, row, column, nextPlayerBlack)) //next player means last player now
            {
                boolean flipped = flip(boolboard, row, column, nextPlayerBlack ? STATE.BLACK : STATE.WHITE);
                if (!flipped) {
                    throw new IllegalStateException("Did not flip for move: " + nextMove);
                }
            } else {
                log.warn("IllegalMove: " + lastMove);
                markNextMoves();
                return;
            }

            //first Move is Black
            if (nextPlayerBlack) {
                nextPlayerBlack = false;
                boolean canMove = markNextMoves(boolboard, nextPlayerBlack);
                if (!canMove) {
                    canMove = markNextMoves(boolboard, !nextPlayerBlack);
                    if (canMove) {
                        nextPlayerBlack = true;
                    }
                    //else end of game
                }

            } else {
                boolboard[row][column] = STATE.WHITE;
                nextPlayerBlack = true;
                boolean canMove = markNextMoves(boolboard, nextPlayerBlack);
                if (!canMove) {
                    canMove = markNextMoves(boolboard, !nextPlayerBlack);
                    if (canMove) {
                        nextPlayerBlack = false;
                    }
                    //else end of game
                }

            }


            board = backpose(boolboard);
            //log.info(board);
            //log.info(toString(boolboard));
            //TODO Skipped Moves

            moves.add(lastMove);
            if (wcount + bcount == 64) {

                if (wcount > bcount) {

                    FacesContext context1 = FacesContext.getCurrentInstance();
                    playerwon = context1.getExternalContext().getSessionMap().get("white").toString();
                    FacesContext context3 = FacesContext.getCurrentInstance();
                    playerlost = context3.getExternalContext().getSessionMap().get("black").toString();
                    winner = "Congratulations! " + playerwon + " won the game!!!";
                    Class.forName("org.apache.derby.jdbc.ClientDriver");
                    Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/othello",
                            "othello", "othello");
                    PreparedStatement st = null;
                    ResultSet rs = null;

                    st = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
                    st.setString(1, playerwon);
                    rs = st.executeQuery();
                    int rowCount = 0;
                    while (rs.next()) {


                        rowCount = rs.getInt(3);

                        rowCount++;
                    }
                    st = con.prepareStatement("UPDATE USERS SET GAMESWON = ? where name = ?");
                    st.setInt(1, rowCount);
                    st.setString(2, playerwon);
                    st.executeUpdate();


                    PreparedStatement st2 = null;
                    ResultSet rs2 = null;
                    st2 = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
                    st2.setString(1, playerlost);
                    rs2 = st2.executeQuery();
                    int rowCount2 = 0;
                    while (rs2.next()) {


                        rowCount2 = rs2.getInt(4);

                        rowCount2++;
                    }
                    st2 = con.prepareStatement("UPDATE USERS SET GAMESLOST = ? where name = ?");
                    st2.setInt(1, rowCount2);
                    st2.setString(2, playerlost);
                    st2.executeUpdate();

                } else {

                    FacesContext context2 = FacesContext.getCurrentInstance();
                    playerwon = context2.getExternalContext().getSessionMap().get("black").toString();
                    FacesContext context4 = FacesContext.getCurrentInstance();
                    playerlost = context4.getExternalContext().getSessionMap().get("white").toString();
                    winner = "Congratulations! " + playerwon + " won the game!!!";

                    Class.forName("org.apache.derby.jdbc.ClientDriver");
                    Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/othello",
                            "othello", "othello");
                    PreparedStatement st = null;
                    ResultSet rs = null;

                    st = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
                    st.setString(1, playerwon);
                    rs = st.executeQuery();
                    int rowCount = 0;
                    while (rs.next()) {


                        rowCount = rs.getInt(3);

                        rowCount++;
                    }
                    st = con.prepareStatement("UPDATE USERS SET GAMESWON = ? where name = ?");
                    st.setInt(1, rowCount);
                    st.setString(2, playerwon);
                    st.executeUpdate();


                    PreparedStatement st2 = null;
                    ResultSet rs2 = null;
                    st2 = con.prepareStatement("SELECT ID, NAME,GAMESWON, GAMESLOST FROM USERS where name=?");
                    st2.setString(1, playerlost);
                    rs2 = st2.executeQuery();
                    int rowCount2 = 0;
                    while (rs2.next()) {


                        rowCount2 = rs2.getInt(4);

                        rowCount2++;
                    }
                    st2 = con.prepareStatement("UPDATE USERS SET GAMESLOST = ? where name = ?");
                    st2.setInt(1, rowCount2);
                    st2.setString(2, playerlost);
                    st2.executeUpdate();



                }
            } else {
                log.info("Game Over");
            }
        }

    }

    private boolean markNextMoves(STATE[][] board, boolean black) {
        boolean marked = false;
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (board[row][column] == null) {
                    if (isLegalMove(board, row, column, black)) {
                        STATE selState = black ? STATE.SELECTABLE_BLACK : STATE.SELECTABLE_WHITE;
                        board[row][column] = selState;
                        marked = true;
                    }
                } else {
                    continue; //Field is occupied
                }
            }
        }

        return marked;
    }

    public static enum STATE {

        WHITE, BLACK, SELECTABLE_BLACK, SELECTABLE_WHITE, NOT_SELECTABLE
    }
    private LinkedList<String> moves = new LinkedList<String>();
    private String board = STARTING_POSITION;
    private boolean nextPlayerBlack = true;
    private int blackStones = 0;
    private int whiteStones = 0;
    private String winner = "Game in progress";

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Creates a new Othello-Board with starting positions.
     */
    public Board() {
        super();
    }

    void reset() {
        nextPlayerBlack = true;
        board = STARTING_POSITION;
        moves.clear();
        markNextMoves();
    }

    public GameView getView() {
        return new GameView(this);
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public LinkedList<String> getMoves() {
        return moves;
    }

    public void setMoves(LinkedList<String> moves) {
        this.moves = moves;
    }

    @Override
    public String toString() {
        return "Board{" + "moves=" + moves + ", board=" + board + ", lastmove=" + getLastMove() + ", nextPlayerBlack=" + nextPlayerBlack + '}';
    }

    public boolean getNextPlayerBlack() {
        return nextPlayerBlack;
    }

    public String getLastMove() {
        if (moves.size() > 0) {
            return moves.getLast();
        }
        return "start";
    }

    public int getBlackStones() {
        return blackStones;
    }

    public int getWhiteStones() {
        return whiteStones;
    }
}