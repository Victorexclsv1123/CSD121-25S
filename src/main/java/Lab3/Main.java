package Lab3;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {




        char[][] board = new char[3][3];

        // Initialize the board with spaces
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                board[row][col] = ' ';
            }
        }

        char player = 'X';
        boolean gameOver = false;
        Scanner scanner = new Scanner(System.in);

        while (!gameOver) {
            printBoard(board);
            System.out.println("Player " + player + ", enter your move (row and column):");

            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row < 0 || row > 2 || col < 0 || col > 2) {
                System.out.println("Invalid move. Please enter values between 0 and 2.");
                continue;
            }

            if (board[row][col] == ' ') {
                board[row][col] = player;

                if (Winner(board, player)) {
                    printBoard(board);
                    System.out.println("Congratulations! Player " + player + " wins!");
                    gameOver = true;
                } else if (isBoardFull(board)) {
                    printBoard(board);
                    System.out.println("It's a draw!");
                    gameOver = true;
                } else {
                    player = (player == 'X') ? 'O' : 'X'; // switch player
                }

            } else {
                System.out.println("Invalid move. Cell already taken.");
            }

            System.out.print("Play again? (y/n): ");
            String again = scanner.next();
            if (!again.equalsIgnoreCase("y")) {
                break;
            }


        }

        scanner.close();
    }


    public static void printBoard(char[][] board) {
        System.out.println("Current Board:");
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                System.out.print("[" + board[row][col] + "]");
            }
            System.out.println();
        }
    }

    public static boolean Winner(char[][] board, char player) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    public static boolean isBoardFull(char[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
