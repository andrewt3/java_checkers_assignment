package org.example.checkers;

import org.example.checkers.io.IOHandler;
import org.example.checkers.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.example.checkers.Constants.BLACK;
import static org.example.checkers.Constants.RED;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CheckersTest {
    private Board board;
    private Player blackPlayer;
    private Player redPlayer;
    private IOHandler ioHandler;
    private Checkers checkers;

    @Before
    public void setUp() {
        board = Mockito.mock(Board.class);
        blackPlayer = Mockito.mock(Player.class);
        redPlayer = Mockito.mock(Player.class);
        ioHandler = Mockito.mock(IOHandler.class);
        
        checkers = new Checkers(board, blackPlayer, redPlayer, ioHandler);
    }

    @Test
    public void testRedPlayerStartsGame() {
        when(redPlayer.getColor()).thenReturn(RED);
        when(blackPlayer.getColor()).thenReturn(BLACK);
        when(board.isMoveValid(any(Move.class), eq(RED))).thenReturn(true);
        when(redPlayer.getMove()).thenReturn(new Move(5, 0, 4, 1));
        when(board.hasValidMoves(RED)).thenReturn(true);

        checkers.playGame(1);

        verify(ioHandler, atLeastOnce()).print("Red's turn");
        verify(board).movePiece(any(Move.class));
    }

    @Test
    public void testPlayerSwitchAfterValidMove() {
        when(redPlayer.getColor()).thenReturn(RED);
        when(blackPlayer.getColor()).thenReturn(BLACK);

        when(board.isMoveValid(any(Move.class), eq(RED))).thenReturn(true);
        when(board.isMoveValid(any(Move.class), eq(BLACK))).thenReturn(true);

        when(redPlayer.getMove()).thenReturn(new Move(5, 0, 4, 1)); // Red moves
        when(blackPlayer.getMove()).thenReturn(new Move(2, 1, 3, 2)); // Black moves

        when(board.hasValidMoves(RED)).thenReturn(true);
        when(board.hasValidMoves(BLACK)).thenReturn(true);

        checkers.playGame(2);

        verify(ioHandler, atLeastOnce()).print("Red's turn");
        verify(ioHandler, atLeastOnce()).print("Black's turn");
        verify(board, times(2)).movePiece(any(Move.class));
    }

    @Test
    public void testEnforceMandatoryJumpsForRed() {
        when(redPlayer.getColor()).thenReturn(RED);
        when(board.hasMandatoryJumps(RED)).thenReturn(true);
        when(board.isMoveValid(any(Move.class), eq(RED))).thenReturn(true);
        when(redPlayer.getMove()).thenReturn(new Move(4, 1, 2, 3)); // Jump move

        checkers.playGame(1);

        verify(ioHandler, atLeastOnce()).print("You must make a jump move!");
        verify(board).movePiece(any(Move.class));
    }

    @Test
    public void testHandleInvalidMove() {
        when(redPlayer.getColor()).thenReturn(RED);
        when(board.isMoveValid(any(Move.class), eq(RED))).thenReturn(false);
        when(redPlayer.getMove()).thenReturn(new Move(5, 0, 4, 4)); // Invalid move
        when(board.hasValidMoves(RED)).thenReturn(true);

        checkers.playGame(1);

        verify(ioHandler, atLeastOnce()).print("Invalid move. Try again.");
        verify(board, never()).movePiece(any(Move.class));
    }

    @Test
    public void testGameEndsWhenNoMovesAvailableForRed() {
        when(redPlayer.getColor()).thenReturn(RED);
        when(board.hasMandatoryJumps(RED)).thenReturn(false);
        when(board.hasValidMoves(RED)).thenReturn(false);

        checkers.playGame(1);

        verify(ioHandler).print("Black wins!");
        verify(ioHandler).print("Game over.");
    }
}