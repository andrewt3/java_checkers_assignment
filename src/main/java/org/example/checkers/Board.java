package org.example.checkers;

import org.example.checkers.io.IOHandler;

import static org.example.checkers.Constants.BLACK;
import static org.example.checkers.Constants.BLACK_KING;
import static org.example.checkers.Constants.RED;
import static org.example.checkers.Constants.RED_KING;

/**
 * Manages the game board and all related validation, movement, and state transitions.
 */
public class Board {
    private static final int SIZE = 8;
    private static final char EMPTY = '.';
    private final char[][] board;

    public Board() {
        board = new char[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if ((row + col) % 2 == 1) { // Dark squares only
                    if (row < 3) {
                        board[row][col] = BLACK;
                    } else if (row > 4) {
                        board[row][col] = RED;
                    } else {
                        board[row][col] = EMPTY;
                    }
                } else {
                    board[row][col] = EMPTY;
                }
            }
        }
    }

    public void printBoard(IOHandler ioHandler) {
        ioHandler.print("  0 1 2 3 4 5 6 7");
        for (int row = 0; row < SIZE; row++) {
            StringBuilder rowString = new StringBuilder(row + " ");
            for (int col = 0; col < SIZE; col++) {
                rowString.append(board[row][col]).append(" ");
            }
            ioHandler.print(rowString.toString());
        }
    }

    public char getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, char piece) {
        board[row][col] = piece;
    }

    /**
     * Executes a move on the board, including jumps if applicable.
     */
    public void movePiece(Move move) {
        char piece = getPiece(move.getFromRow(), move.getFromCol());
        setPiece(move.getToRow(), move.getToCol(), piece); // Place piece in target position
        setPiece(move.getFromRow(), move.getFromCol(), EMPTY); // Clear original position

        // If it's a jump move, remove the jumped piece
        if (Math.abs(move.getToRow() - move.getFromRow()) == 2) {
            int jumpRow = (move.getFromRow() + move.getToRow()) / 2;
            int jumpCol = (move.getFromCol() + move.getToCol()) / 2;
            setPiece(jumpRow, jumpCol, EMPTY); // Remove the captured piece
        }

        // Handle King promotion
        if (move.getToRow() == 0 && piece == RED) {
            setPiece(move.getToRow(), move.getToCol(), RED_KING);
        } else if (move.getToRow() == 7 && piece == BLACK) {
            setPiece(move.getToRow(), move.getToCol(), BLACK_KING);
        }
    }

    /**
     * Checks whether a move is valid for the specified player.
     */
    public boolean isMoveValid(Move move, char playerColor) {
        int fromRow = move.getFromRow();
        int fromCol = move.getFromCol();
        int toRow = move.getToRow();
        int toCol = move.getToCol();

        // Ensure coordinates are within bounds
        if (!isInBounds(fromRow, fromCol) || !isInBounds(toRow, toCol)) {
            return false;
        }

        // Ensure the destination square is empty
        if (getPiece(toRow, toCol) != EMPTY) {
            return false;
        }

        char piece = getPiece(fromRow, fromCol);

        // Ensure the piece belongs to the player
        if (playerColor == BLACK && !(piece == BLACK || piece == BLACK_KING)) return false;
        if (playerColor == RED && !(piece == RED || piece == RED_KING)) return false;

        int rowDiff = toRow - fromRow;
        int colDiff = Math.abs(toCol - fromCol);

        // Regular jump
        if (Math.abs(rowDiff) == 2 && colDiff == 2) {
            int jumpRow = (fromRow + toRow) / 2;
            int jumpCol = (fromCol + toCol) / 2;

            char jumpedPiece = getPiece(jumpRow, jumpCol);

            // Ensure the jumped piece belongs to the opponent
            if (playerColor == BLACK && (jumpedPiece == RED || jumpedPiece == RED_KING)) {
                return true;
            } else if (playerColor == RED && (jumpedPiece == BLACK || jumpedPiece == BLACK_KING)) {
                return true;
            }

            return false;
        }

        // Regular move (1 diagonal step)
        if (Math.abs(rowDiff) == 1 && colDiff == 1) {
            if (piece == BLACK && rowDiff == 1) return true; // Black piece moves forward
            if (piece == RED && rowDiff == -1) return true; // Red piece moves forward
            if (piece == BLACK_KING || piece == RED_KING) return true; // Kings move any direction
        }

        return false; // Invalid move
    }

    /**
     * Checks if the specified player has any jumps available (mandatory jumps).
     */
    public boolean hasMandatoryJumps(char playerColor) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char piece = getPiece(row, col);
                if ((playerColor == BLACK && (piece == BLACK || piece == BLACK_KING)) ||
                        (playerColor == RED && (piece == RED || piece == RED_KING))) {
                    if (canJump(row, col, playerColor)) {
                        return true; // At least one jump is available
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines whether the specified player has any valid moves (either regular moves or jumps).
     */
    public boolean hasValidMoves(char playerColor) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char piece = getPiece(row, col);

                if ((playerColor == BLACK && (piece == BLACK || piece == BLACK_KING)) ||
                        (playerColor == RED && (piece == RED || piece == RED_KING))) {
                    if (canMove(row, col, playerColor) || canJump(row, col, playerColor)) {
                        return true; // At least one valid move exists
                    }
                }
            }
        }
        return false; // No valid moves found
    }

    /**
     * Checks if the piece at a given position can make a regular move.
     */
    private boolean canMove(int fromRow, int fromCol, char playerColor) {
        int[] rowDiffs = {-1, -1, 1, 1}; // Possible move directions
        int[] colDiffs = {-1, 1, -1, 1};

        for (int i = 0; i < rowDiffs.length; i++) {
            int toRow = fromRow + rowDiffs[i];
            int toCol = fromCol + colDiffs[i];
            Move move = new Move(fromRow, fromCol, toRow, toCol);
            if (isMoveValid(move, playerColor)) {
                return true; // A valid move exists
            }
        }
        return false;
    }

    /**
     * Checks if the piece at a given position can make a jump.
     */
    private boolean canJump(int fromRow, int fromCol, char playerColor) {
        int[] rowDiffs = {-2, -2, 2, 2}; // Possible jump directions
        int[] colDiffs = {-2, 2, -2, 2};

        for (int i = 0; i < rowDiffs.length; i++) {
            int toRow = fromRow + rowDiffs[i];
            int toCol = fromCol + colDiffs[i];
            Move move = new Move(fromRow, fromCol, toRow, toCol);
            if (isMoveValid(move, playerColor)) {
                return true; // A valid jump exists
            }
        }
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    public void cleanBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                board[row][col] = EMPTY;
            }
        }
    }
}