package org.example.checkers.io;

import java.util.Scanner;

public class ConsoleIOHandler implements IOHandler {
    private final Scanner scanner;

    public ConsoleIOHandler() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public String readLine() {
        return scanner.nextLine();
    }

    @Override
    public int readInt() {
        return scanner.nextInt();
    }
}