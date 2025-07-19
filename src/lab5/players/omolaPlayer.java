package lab5.players;

import lab5.game.Board;
import lab5.game.PlayerToken;
import lab5.game.Position;

public class omolaPlayer extends Player {

    public omolaPlayer(String name) {
        super(name);
    }

    @Override
    public Position pickNextMove(Board board) {
        PlayerToken myToken = board.getNextTurnToken();
        PlayerToken opponent = myToken.opponent();

        // 1. Try to win
        for (Position pos : board.getEmptyCells()) {
            Board copy = new Board(board);
            copy.placeNextToken(pos); // simulate own move

            if ((copy.getStatus() == Board.Status.XWins || copy.getStatus() == Board.Status.OWins)
                    && copy.getWinner() == myToken) {
                return pos;
            }
        }

        // 2. Try to block opponent
        for (Position pos : board.getEmptyCells()) {
            Board copy = new Board(board);

            if (opponent == PlayerToken.X) {
                copy.placeX(pos);
            } else {
                copy.placeO(pos);
            }

            if ((copy.getStatus() == Board.Status.XWins || copy.getStatus() == Board.Status.OWins)
                    && copy.getWinner() == opponent) {
                return pos;
            }
        }

        // 3. Pick first available move
        return board.getEmptyCells().get(0);
    }
}
