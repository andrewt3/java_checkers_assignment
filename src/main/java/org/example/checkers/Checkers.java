package org.example.checkers;

import org.example.checkers.io.IOHandler;
import org.example.checkers.player.Player;

import static org.example.checkers.Constants.BLACK;

public class Checkers {
    private final Board board;
    private final Player blackPlayer;
    private final Player redPlayer;
    private final IOHandler ioHandler;

    public Checkers(Player blackPlayer, Player redPlayer, IOHandler ioHandler) {
        this.board = new Board();
        this.blackPlayer = blackPlayer;
        this.redPlayer = redPlayer;
        this.ioHandler = ioHandler;
    }

    public Checkers(Board board, Player blackPlayer, Player redPlayer, IOHandler ioHandler) {
        this.board = board;
        this.blackPlayer = blackPlayer;
        this.redPlayer = redPlayer;
        this.ioHandler = ioHandler;
    }

    public void playGame() {
        playGame(Integer.MAX_VALUE);
    }

    public void playGame(int turnLimit) {
        Player currentPlayer = redPlayer;
        int turns = 0;

        while (turns < turnLimit) {
            board.printBoard(ioHandler);
            ioHandler.print((currentPlayer.getColor() == BLACK ? "Black's" : "Red's") + " turn");

            // Check if the current player has any valid moves or jumps
            boolean mustJump = board.hasMandatoryJumps(currentPlayer.getColor());
            boolean hasValidMoves = board.hasValidMoves(currentPlayer.getColor());

            // If no valid moves or jumps are available, end the game
            if (!hasValidMoves && !mustJump) {
                ioHandler.print((currentPlayer.getColor() == BLACK ? "Red wins!" : "Black wins!"));
                ioHandler.print("Game over.");
                break;
            }

            if (mustJump) {
                ioHandler.print("You must make a jump move!");
            }

            // Get the player's move
            Move move = currentPlayer.getMove();

            // Validate the move: enforce mandatory jumps if required
            if ((mustJump && board.isMoveValid(move, currentPlayer.getColor()) &&
                    Math.abs(move.getFromRow() - move.getToRow()) == 2) || // Valid jump move
                    (!mustJump && board.isMoveValid(move, currentPlayer.getColor()))) { // Valid normal move
                board.movePiece(move);

                // Switch players
                currentPlayer = (currentPlayer == blackPlayer) ? redPlayer : blackPlayer;
            } else {
                ioHandler.print("Invalid move. Try again.");
            }

            turns++;
        }

        if (turns >= turnLimit) {
            ioHandler.print("Test mode: Exiting after reaching the turn limit.");
        }
    }
}