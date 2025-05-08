package org.example.checkers.player;

import org.example.checkers.Move;
import org.example.checkers.io.IOHandler;

public class HumanPlayer implements Player {
    private final char color;
    private final IOHandler ioHandler;

    public HumanPlayer(char color, IOHandler ioHandler) {
        this.color = color;
        this.ioHandler = ioHandler;
    }

    @Override
    public char getColor() {
        return color;
    }

    @Override
    public Move getMove() {
        ioHandler.print("Enter the row and column of the piece to move (e.g., 2 3): ");
        int fromRow = ioHandler.readInt();
        int fromCol = ioHandler.readInt();
        ioHandler.print("Enter the row and column of the destination (e.g., 3 4): ");
        int toRow = ioHandler.readInt();
        int toCol = ioHandler.readInt();
        return new Move(fromRow, fromCol, toRow, toCol);
    }
}