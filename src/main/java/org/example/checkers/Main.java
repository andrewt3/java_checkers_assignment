package org.example.checkers;

import org.example.checkers.io.ConsoleIOHandler;
import org.example.checkers.io.IOHandler;
import org.example.checkers.player.HumanPlayer;
import org.example.checkers.player.Player;

import static org.example.checkers.Constants.BLACK;
import static org.example.checkers.Constants.RED;

public class Main {

    public static void main(String[] args) {
        IOHandler ioHandler = new ConsoleIOHandler();
        Player blackPlayer = new HumanPlayer(BLACK, ioHandler);
        Player redPlayer = new HumanPlayer(RED, ioHandler);

        Checkers game = new Checkers(blackPlayer, redPlayer, ioHandler);
        game.playGame();
    }
}
