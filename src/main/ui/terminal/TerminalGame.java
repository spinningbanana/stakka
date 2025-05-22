package ui.terminal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;

import model.Game;
import model.Score;
import model.Seg;
import model.pieces.Piece;
import persistence.JsonLoader;
import persistence.JsonWriter;
import ui.exceptions.InvalidInputException;

/*
 * Represents the very basic console ui of the game
 * responsible for changing user keybinds
 * responsible for generating frames from Game.getBoard()
 * responsible for key inputs to control the falling piece
 */
public class TerminalGame {
    private static final String ANSI_CLEAR = "\033[H\u001B[2J";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_UNDERLINE = "\u001B[4m";
    private static final String ANSI_RESET = "\u001B[0m";

    private static final String ANSI_CYAN = "\033[48;2;0;255;255m";
    private static final String ANSI_YELLOW = "\033[48;2;255;255;0m";
    private static final String ANSI_PURPLE = "\033[48;2;255;0;255m";
    private static final String ANSI_BLUE = "\033[48;2;0;0;255m";
    private static final String ANSI_GREEN = "\033[48;2;0;255;0m";
    private static final String ANSI_RED = "\033[48;2;255;0;0m";
    private static final String ANSI_ORANGE = "\033[48;2;255;165;0m";
    private static final String ANSI_BLACK = "\033[48;2;50;50;50m" + "\033[38;2;25;25;25m";

    private static final int HOLD_ROW = 4;
    private static final int NEXT_ROW = 5;
    private static final int SCORE_ROW = 22;

    private char keyLeft = 'k';
    private char keyRight = ';';
    private char keySD = 'l';
    private char keyHD = ' ';
    private char keyRotateCW = 'o';
    private char keyRotateCounterCW = 'a';
    private char keyHold = 'c';

    static final String STR_SAVE = "SAVE";
    static final String STR_LOAD = "LOAD";
    
    static final String DEFAULT_SAVE = "./data/default.json";

    private Game game;
    private Scanner input;
    private JsonWriter writer;

    // EFFECTS: generates frames until game is lost
    //          handles key inputs
    //          initially asks if the player wishes to change their key bindings
    //          clears the console every "frame"
    public TerminalGame() {
        input = new Scanner(System.in);
        
        System.out.println("Welcome! Please make sure the console is tall enough to see 23 lines of a board!");
        changeKeyBindings();

        game = new Game();

        writer = new JsonWriter(game);

        boolean playing = true;
        while (playing) {
            while (game.getStatus()) {
                clearConsole();
                printBoard();
                showHelp();
                handleInput();
            }
            showGameSummary();

            System.out.print("\nThanks for playing! Would you like to play again? (y/n) ");
            if (input.nextLine().toLowerCase().equals("y")) {
                game.newGame();
            } else {
                playing = false;
            }
        }  

        System.out.println("Goodbye!");
        input.close();
    }

    // EFFECTS: prints ANSI_CLEAR and then flushes the console
    private void clearConsole() {
        System.out.print(ANSI_CLEAR); // clear console
        System.out.flush();
    }

    // EFFECTS: prints the game summary,
    //              - final score
    //              - high scores
    //              - whether the player wishes to save their score
    private void showGameSummary() {
        clearConsole();

        System.out.println(ANSI_BOLD + ANSI_UNDERLINE + "GAME OVER!" + ANSI_RESET);

        System.out.println("\nYour final score is: " + ANSI_YELLOW + game.getScore() + ANSI_RESET);

        if (game.getHighScores().size() > 0) {
            String leftAlignFormat = "%-15s %-4d %n";

            System.out.println(ANSI_BOLD + "\nHigh Scores" + ANSI_RESET);
            System.out.format("NAME            SCORE %n");
            for (Score score : game.getHighScores()) {
                System.out.format(leftAlignFormat, score.getName(), score.getScore());
            }
        }

        System.out.print("\nWould you like to save your score? (y/n) ");
        if (input.nextLine().toLowerCase().equals("y")) {
            System.out.print("\nChoose a name: ");
            String name = input.nextLine();
            game.addHighScore(new Score(name, game.getScore()));
        }
    }

    // MODIFIES: this
    // EFFECTS: asks if the user wishes to change key bindings, accepts new key bindings if yes
    //          otherwise do nothing
    private void changeKeyBindings() {
        System.out.print("Would you like to set custom key binds? (y/n) ");

        if (input.nextLine().toLowerCase().equals("y")) {
            System.out.print("Move left: ");
            keyLeft = input.nextLine().toLowerCase().charAt(0);
            System.out.print("Move right: ");
            keyRight = input.nextLine().toLowerCase().charAt(0);
            System.out.print("Rotate clockwise: ");
            keyRotateCW = input.nextLine().toLowerCase().charAt(0);
            System.out.print("Rotate counter-clockwise: ");
            keyRotateCounterCW = input.nextLine().toLowerCase().charAt(0);
            System.out.print("Soft drop: ");
            keySD = input.nextLine().toLowerCase().charAt(0);
            System.out.print("Hard drop: ");
            keyHD = input.nextLine().toLowerCase().charAt(0);

        }
    }

    // EFFECTS: handle user input, get valid keys
    private void handleInput() {
        char key = 'X';
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Your input: ");

            String keyFull = input.nextLine();
            if (keyFull.length() > 0) {

                validInput = true;
                key = keyFull.toLowerCase().charAt(0);

                if (keyFull.equals(STR_SAVE)) {
                    saveSequence();
                } else if (keyFull.equals(STR_LOAD)) {
                    loadSequence();
                } else if (key == keyLeft || key == keyRight || key == keySD || key == keyHD || key == keyRotateCW 
                            || key == keyRotateCounterCW || key == keyHold) {
                    mapInput(key);
                } else {
                    validInput = false;
                }
            
            }
        }

        if (key == 'X') {
            throw new InvalidInputException();
        }
    }

    // EFFECTS: maps inputs to game activities
    private void mapInput(char key) {
        Piece piece = game.getCurrentPiece();

        // switch statements only take constants :(
        if (key == keyLeft) {
            piece.move(-1, 0);
        } else if (key == keyRight) {
            piece.move(+1, 0);
        } else if (key == keySD) {
            piece.move(false);
        } else if (key == keyRotateCW) {
            piece.rotateCW();
        } else if (key == keyRotateCounterCW) {
            piece.rotateCounterCW();
        } else if (key == keyHD) {
            piece.hardDrop();
        } else if (key == keyHold) {
            game.cycleHoldPiece();
        }
    }

    // EFFECTS: prints the current game.getBoard() in ASCII with the game.getCurrentPiece()
    //          with colours!! (using ANSI codes) :D
    private void printBoard() {
        String[][] board = game.getBoard();

        Piece currPiece = game.getCurrentPiece();

        game.updatePiecePreview();

        StringBuilder rows = new StringBuilder();
        for (int y = 0; y < board.length; y++) {
            
            for (int x = 0; x < board[y].length; x++) {
                displayCellElement(rows, currPiece, board[y][x], x, y);
            }

            displayUIElements(rows, y);
        }
        System.out.println(rows.toString());
    }

    // MODIFIES: row
    // EFFECTS: adds the score on line SCORE_ROW and the next 4 pieces on line NEXT_ROW, also adds a new line
    private void displayUIElements(StringBuilder row, int y) {
        row.append(ANSI_RESET + ANSI_BOLD);
        if (y == NEXT_ROW) {
            List<String> bag = game.getBag();
            row.append("\tNext: " + bag.get(0) + " " + bag.get(1) + " " + bag.get(2) + " " + bag.get(3) + " ");
        } else if (y == SCORE_ROW) {
            row.append("\tScore: " + game.getScore());
        } else if (y == HOLD_ROW) {
            row.append("\tHold: " + game.getHeldPiece());
        }
        row.append(ANSI_RESET + "\n");
    }

    // MODIFIES: row
    // EFFECTS: displays what the inputted cell currently looks like
    private void displayCellElement(StringBuilder rows, Piece currPiece, String element, int x, int y) {
        Seg[] segments = currPiece.getConfiguration();

        boolean pieceIsHere = isPieceHere(segments, currPiece.getCenterX(), currPiece.getCenterY(), x, y);
        boolean previewIsHere = isPieceHere(segments, currPiece.getCenterX(), game.getDroppedY(), x, y);
        if (pieceIsHere) {
            chooseColour(currPiece.toString(), rows);
        } else if (element == null || previewIsHere) {
            rows.append(ANSI_BLACK);
        } else {
            chooseColour(element, rows);
        }

        if (previewIsHere && !pieceIsHere) {
            rows.append("[]");
        } else if (y > 3 || pieceIsHere || element != null) {
            rows.append("  ");
        } else {
            rows.append(ANSI_RESET + "  ");
        }
    }

    // EFFECTS: returns true if a segment is at (x, y)
    private boolean isPieceHere(Seg[] segments, int pieceX, int pieceY, int x, int y) {
        for (Seg seg : segments) {
            int posX = pieceX + seg.getX();
            int posY = pieceY + seg.getY();

            if (posX == x && posY == y) {
                return true;
            }
        }
        return false;
    }

    // MODIFIES: rows
    // EFFECTS: adds the appropriate piece colour to row
    private void chooseColour(String piece, StringBuilder row) {
        switch (piece) {
            case "I":
                row.append(ANSI_CYAN);
                break;
            case "T":
                row.append(ANSI_PURPLE);
                break;
            case "O":
                row.append(ANSI_YELLOW);
                break;
            case "L":
                row.append(ANSI_ORANGE);
                break;
            case "J":
                row.append(ANSI_BLUE);
                break;
            case "S":
                row.append(ANSI_GREEN);
                break;
            case "Z":
                row.append(ANSI_RED);
                break;
            // default:
            //     row.append(ANSI_RESET);
            //     break;
        }
    }

    // EFFECTS: prints the controls of the game
    private void showHelp() {
        System.out.println(ANSI_RESET + ANSI_BOLD + ANSI_UNDERLINE + "Help:" + ANSI_RESET);
        System.out.println("Move left: " + keyLeft + "\t\tMove right: " + keyRight + "\t\tSoft drop: " + keySD);
        System.out.print("Hard drop: ");
        if (keyHD == ' ') {
            System.out.print("SPACE");
        } else {
            System.out.print(keyHD);
        }
        System.out.print("\tRotate CW: " + keyRotateCW + "\t\tRotate CCW: " + keyRotateCounterCW);
        System.out.println("\nHold: " + keyHold + "\t\t\tSave to file: " 
                            + STR_SAVE + "\tLoad from file: " + STR_LOAD);
        System.out.println();
    }

    // EFFECTS: user inputs required to save the current game state to file
    private void saveSequence() {
        boolean saved = false;

        while (!saved) {
            String file = DEFAULT_SAVE;
            System.out.print("Save to default? (y/n/abort) ");

            String next = input.nextLine();
            if (next.length() == 0 || next.equals("abort")) {
                break;
            } else if (next.toLowerCase().charAt(0) != 'y') {
                System.out.println("What would you like your save to be named?");
                file = "./data/" + input.nextLine() + ".json";
            }

            try {
                writer.write(file);
                saved = true;
            } catch (FileNotFoundException e) {
                System.out.println(ANSI_BOLD + ANSI_RED + "UNABLE TO SAVE TO "
                                    + ANSI_RESET + file);
            }
        }
    }

    // EFFECTS: user inputs required to load a game state from file
    private void loadSequence() {
        boolean loaded = false;

        while (!loaded) {
            String file = DEFAULT_SAVE;
            System.out.print("Load from default? (y/n/abort) ");

            String next = input.nextLine().toLowerCase();
            if (next.length() == 0 || next.equals("abort")) {
                break;
            } else if (next.charAt(0) == 'n') {
                System.out.print("Load from where?\n./data/");
                file = "./data/" + input.nextLine();
            }

            try {
                JsonLoader loader = new JsonLoader(file, game);
                loader.loadGameState();
                loaded = true;
            } catch (IOException e) {
                System.out.println(ANSI_BOLD + ANSI_RED + "UNABLE TO LOAD FROM " + ANSI_RESET + file);
            } catch (JSONException e) {
                System.out.println(ANSI_BOLD + ANSI_RED + "SOMETHING IS WRONG WITH FILE "
                                    + ANSI_RESET + file + "");
                System.out.println(ANSI_BOLD + ANSI_RED + "ISSUE: " + ANSI_RESET + e.getMessage());
            }
        }
    }
}
