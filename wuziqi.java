import java.util.Scanner;
import java.lang.Exception;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class wuziqi {
    private static Scanner scanner = new Scanner(System.in);

    private static final int SIZE = initSize();
    private static final String letters = "abcdefghijklmnopqrstuvwxyz";
    private static final String EMPTY = "\u001B[90m" + "+" + "\u001B[0m";
    private static final String PLAYER = "\u001B[37m" + "●" + "\u001B[0m";
    private static final String AI = "\u001B[37m" + "○" + "\u001B[0m";
    private static final LocalDateTime timeNow = LocalDateTime.now();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd_HH-mm-ss");
    private static final String time = timeNow.format(formatter);

    private static String[][] board = new String[SIZE][SIZE];
    private static File file = new File("./Log/", "log_" + time + ".txt");
        
    private static int initSize() {
        System.out.println();
        System.out.println("Select the size of the board:");
        System.out.println("input '1' for 13x13 (small)");
        System.out.println("input '2' for 17x17 (medium)");
        System.out.println("input '3' for 19x19 (large)");
        System.out.println("or input any number between 9-26 to customize the size");

        int Size = 0;
        do {
            try {
                System.out.print("> ");
                String tempSize = scanner.next();
                Size = Integer.valueOf(tempSize);
                if (Size == 1) {
                    System.out.println("set size to small\n");
                    return 13;
                } else if (Size == 2) {
                    System.out.println("set size to medium\n");
                    return 17;
                } else if (Size == 3) {
                    System.out.println("set size to large\n");
                    return 19;
                } else if (Size >= 9 && Size <= 26) {
                    System.out.println("set size to " + Size + "x" + Size + ".\n");
                    return Size;
                } else {
                    System.out.println("please select 1, 2, 3; or input between 9 and 26!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("please select 1, 2, 3; or input between 9 and 26!");
            }
        } while (true);
    }

    private static void printBoard() {
        // print letter index
        System.out.print("   ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(letters.charAt(i) + " ");
        }
        // print board
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            // print number index before each line
            if (i < 9) {
                System.out.print(" " + (i + 1) + " ");
            } else {
                System.out.print((i + 1) + " ");
            }
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static boolean checkWin(int row, int col, String symbol, int role) {
        return checkDirection(row, col, 1, 0, symbol, role) || // Horizontal
               checkDirection(row, col, 0, 1, symbol, role) || // Vertical
               checkDirection(row, col, 1, 1, symbol, role) || // Diagonal (down-right)
               checkDirection(row, col, 1, -1, symbol, role);  // Diagonal (up-right)
    }

    private static boolean checkDirection(int row, int col, int dRow, int dCol, String symbol, int role) {
        int count = 1; // the current move counts as one piece
        int r, c;

        // check forward direction
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == symbol) {
            count++;
            r += dRow;
            c += dCol;
        }
        // check backward (opposite) direction
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == symbol) {
            count++;
            r -= dRow;
            c -= dCol;
        }
        // if the connected piece greater than requires, return True
        return count >= role;
    }

    private static boolean isBoardFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void writeBoard(FileWriter fw) throws IOException{
        // print letter index
        fw.write("\n   ");
        for (int i = 0; i < SIZE; i++) {
            fw.write(letters.charAt(i) + " ");
        }
        // print board
        fw.write("\n");
        for (int i = 0; i < SIZE; i++) {
            // print number index before each line
            if (i < 9) {
                fw.write(" " + (i + 1) + " ");
            } else {
                fw.write((i + 1) + " ");
            }
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    fw.write("+ ");
                } else if (board[i][j] == PLAYER) {
                    fw.write("● ");
                } else if (board[i][j] == AI) {
                    fw.write("○ ");
                }
                
            }
            fw.write("\n");
        }
        fw.write("\n");
    }

    // Please refer to readme for the documtation for robot method.
    private static int[] AI(String[][] board, int lastRow, int lastCol) {
        for (int i = 5; i > 0; i--) {

            // First, check if robot can win in the next move
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = AI;
                        if (checkWin(row, col, AI, i)) {
                            board[row][col] = EMPTY;
                            return new int[]{row, col};
                        }
                        board[row][col] = EMPTY;
                    }
                }
            }

            // If robot can't win in the next move, try to block player
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    if (board[row][col] == EMPTY) {
                        board[row][col] = PLAYER;
                        if (checkWin(row, col, PLAYER, i)) {
                            board[row][col] = EMPTY;
                            return new int[]{row, col};
                        }
                        board[row][col] = EMPTY;
                    }
                }
            }
        }
        // Never happen.
        return new int[]{-1, -1};
    }

    public static void main(String[] args) throws IOException{
        boolean PlayersTurn = true;
        boolean gameEnded = false;
        boolean save = true;
        int row = SIZE / 2;
        int col = SIZE / 2;
        int[] move = new int[2];
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating export file, the current game will not be saved.\n");
            save = false;
        }
        // initialize board
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }

        // set up players
        System.out.println("Who is going to play first?");
        System.out.println("1. You");
        System.out.println("2. Robot");
        while (true) {
            System.out.print("> ");
            String inputCheck = scanner.nextLine();
            if (inputCheck.equals("1")) {
                if (save) {
                    fw.write("The size of the board is " + SIZE + " x " + SIZE + ".\n");
                    fw.write("Player takes the first move.\n");
                    writeBoard(fw);
                }
                break;
            } else if (inputCheck.equals("2")) {
                if (save) {
                    fw.write("The size of the board is " + SIZE + " x " + SIZE + ".\n");
                    fw.write("Robot takes the first move.\n");
                    writeBoard(fw);
                    fw.write("Robot: (" + (row+1) + ", " + letters.charAt(col) + ")\n");
                }
                board[row][col] = AI;
                System.out.println("Robot plays at (" + (row+1) + ", " + letters.charAt(col) + ")\n");
                break;
            } else {
                System.out.println ("invalid selection!");
            }
        }

        // game starts
        while (!gameEnded) {
            String currentPlayer = PlayersTurn ? PLAYER : AI;
            
            // Robot's move
            if (!PlayersTurn) { 
                move = AI(board, row, col);
                row = move[0];
                col = move[1];
                if (save) {
                    fw.write("Robot: (" + (row+1) + ", " + letters.charAt(col) + ")\n");
                }
                System.out.println("Robot plays at (" + (row+1) + ", " + letters.charAt(col) + ")\n");
                
            
            // Player's move (ask for input)
            } else {
                printBoard();
                System.out.println("Your turn (" + currentPlayer + "): Enter row and column index (row col):");
                do {
                    try {
                        System.out.print("> ");
                        String input = scanner.nextLine();
                        String[] coordinate = input.split(" ");
                        row = Integer.valueOf(coordinate[0]) - 1;
                        col = letters.indexOf(coordinate[1]);
                        if (col == -1) {
                            System.out.println("Invalid input, try again.");
                        } else {
                            if (save) {
                                fw.write("Human: (" + (row+1) + ", " + letters.charAt(col) + ")\n");
                            }
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input, try again.");
                    }
                } while (true);
            }

            // check location validity
            if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY) {
                board[row][col] = currentPlayer;
                
                // check if move cause win
                if (checkWin(row, col, currentPlayer, 5)) {
                    printBoard();
                    gameEnded = true;
                    System.out.println((PlayersTurn ? "Player" : "Robot") + " (" + currentPlayer + ") wins!");
                    if (save) {
                        writeBoard(fw);
                        fw.write((PlayersTurn ? "Human" : "Robot") + " wins!");
                    }
                // check if move cause draw
                } else if (isBoardFull()) {
                    printBoard();
                    gameEnded = true;
                    System.out.println("The game is a draw!");
                    if (save) {
                        writeBoard(fw);
                        fw.write("The game is a draw!");
                    }
                
                // else, switch player and game continues.
                } else {
                    PlayersTurn = !PlayersTurn;
                }
            } else {
                System.out.println("Invalid input, try again.");
            }
        }
        scanner.close();
        if (save) {
            fw.close();
        }
    }
}
