package org.example.checkers;

import org.junit.Test;

import static org.example.checkers.Constants.BLACK;
import static org.example.checkers.Constants.RED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testBoardInitialization() {
        Board board = new Board();

        // Check pieces are correctly placed
        assertEquals('b', board.getPiece(0, 1));
        assertEquals('r', board.getPiece(7, 0));
        assertEquals('.', board.getPiece(4, 4));
    }

    @Test
    public void testMoveValidForRegularMove() {
        Board board = new Board();
        Move move = new Move(5, 0, 4, 1); // Red moves forward diagonally
        assertTrue(board.isMoveValid(move, RED)); // Red valid move
    }

    @Test
    public void testMoveInvalidForOccupiedDestination() {
        Board board = new Board();
        Move move = new Move(5, 0, 5, 2); // Red tries moving diagonally where another Red piece exists
        assertFalse(board.isMoveValid(move, RED)); // Destination not empty
    }

    @Test
    public void testJumpMoveForRed() {
        Board board = new Board();
        board.cleanBoard();

        // Manually set scenario where Red can jump Black
        board.setPiece(4, 1, 'r');
        board.setPiece(3, 2, 'b');
        Move validJump = new Move(4, 1, 2, 3); // Red jumps Black piece at [3,2]
        assertTrue(board.isMoveValid(validJump, 'r'));
    }

    @Test
    public void testHasMandatoryJumpsForRed() {
        Board board = new Board();
        board.cleanBoard();

        // Set scenario where Red has a mandatory jump
        board.setPiece(4, 1, RED);
        board.setPiece(3, 2, BLACK);
        assertTrue(board.hasMandatoryJumps(RED));
    }

    @Test
    public void testNoMandatoryJumps() {
        Board board = new Board();
        assertFalse(board.hasMandatoryJumps('r'));
    }

    @Test
    public void testMovePieceExecution() {
        Board board = new Board();
        // Set jump scenario
        board.setPiece(4, 1, 'r');
        board.setPiece(3, 2, 'b');
        Move jumpMove = new Move(4, 1, 2, 3); // Red jumps Black piece at [3,2]
        board.movePiece(jumpMove);

        // Check board state after move
        assertEquals('r', board.getPiece(2, 3)); // Red piece moved
        assertEquals('.', board.getPiece(4, 1)); // Original Red spot cleared
        assertEquals('.', board.getPiece(3, 2)); // Captured Black piece removed
    }
}