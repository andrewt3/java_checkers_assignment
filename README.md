# **Checkers Game**

Welcome to the **Checkers Game** project, an implementation of the traditional board game "Checkers". This project is written in **Java** and follows a modular, testable, and extensible architecture. The game features customizable player types (e.g., human vs human, human vs Computer), enforcing mandatory jump rules, and game-ending conditions.

The game is based on the rules described here:
https://www.thesprucecrafts.com/play-checkers-using-standard-rules-409287
---

## **Architecture**

The project is structured using a modular design to enhance testability, extensibility, and readability. The following components make up the architecture:

### **1. Core Components**
- **`Move`**:
    - Encapsulates the movement details (`fromRow`, `fromCol`, `toRow`, `toCol`) for a piece.

- **`Board`**:
    - Manages the game board, including the placement of pieces and the validation of moves.
    - Handles King promotion and mandatory jumps.

- **`Checkers`**:
    - Implements the main game logic, including turn management, mandatory jump enforcement, and end-game conditions.
    - Switches between players and interacts with the board.

### **2. Player Abstractions**
- **`Player` Interface**:
    - Abstraction for any type of player (e.g., human, AI).
    - Defines methods like `getColor()` and `getMove()`.

- **`HumanPlayer`**:
    - An implementation of `Player` for human-controlled gameplay.
    - Receives user input for moves via an `IOHandler`.

### **3. Input/Output Management**
- **`IOHandler` Interface**:
    - Abstracts input/output operations for flexibility and testing.

- **`ConsoleIOHandler`**:
    - A standard implementation of `IOHandler`, which interacts with the console for input and output.

### **4. Testability**
- Designed for unit testing using **JUnit** and **Mockito** for mocked interactions.
- Includes `playGame(int turnLimit)` method to limit game turns during testing to avoid infinite loops.

---
