package lab5.ui;

import com.diogonunes.jcolor.AnsiFormat;
import lab5.game.*;
import lab5.players.*;

import java.util.Scanner;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Console {

    // Define some colors and text styles for use in the console
    private static final AnsiFormat fPrompt = new AnsiFormat(GREEN_TEXT(), BOLD());
    private static final AnsiFormat fAlert = new AnsiFormat(YELLOW_TEXT());

    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * Prompt the user for input using the given promptMessage
     * @param promptMessage The message to prompt the user with
     * @return The user's response
     */
    public static String prompt(String promptMessage) {
        System.out.print(fPrompt.format(promptMessage));
        var scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Display an alert message to the user
     * @param message The message to display
     */
    public static void printAlert(String message) {
        System.out.println(fAlert.format(message));
    }

    /**
     * Repeatedly prompt the user to select a player for the given number
     * until they select a valid one of a set of valid players
     * @param playerNumber The player number to show in prompts
     * @return A player object representing the user's chosen player
     */
    public static Player promptForPlayer(int playerNumber) {
        println("Choose player type for Player " + playerNumber + ":");
        println("1. Human");
        println("2. Circe (Simple Computer Player)");
        println("3. Omola (Advanced Computer Player)");

        int choice = Integer.parseInt(prompt("Enter choice: "));
        String name = prompt("Enter name for Player " + playerNumber + ": ");

        return switch (choice) {
            case 1 -> new HumanPlayer(name);
            case 2 -> new CircePlayer(name);
            case 3 -> new omolaPlayer(name);
            default -> new HumanPlayer(name); // fallback
        };
    }

    /**
     * Display the given game board
     * @param board A tictactoe game board
     */
    public static void showBoard(Board board) {
        var sb = new StringBuilder();
        for (var c : board.toString().toCharArray()) {
            if ( c == 'X' ) {
                sb.append(colorize("X", BRIGHT_CYAN_TEXT()));
            } else if ( c == 'O' ) {
                sb.append(colorize("O", BRIGHT_MAGENTA_TEXT()));
            } else {
                sb.append(c);
            }
        }
        System.out.println(sb);
    }

    /**
     * Repeatedly prompt the user for a position on which to place their next token.
     * If they enter an invalid response they are re-prompted.
     * @param prompt The prompt to display to the user
     * @return The position selected by the user
     */
    public static Position promptForPosition(String prompt) {

        final String helpMessage = "Input must be in the format 'row column', e.g., '1 2' or 't m' for the top middle cell.";

        while ( true ) {
            var input = prompt(fPrompt.format(prompt));

            if ( input.length() != 3 ) {
                printAlert(helpMessage);
                continue;
            }

            var parts = input.split(" ");

            if ( parts.length != 2 ) {
                printAlert(helpMessage);
                continue;
            }

            try {
                return new Position(Row.from(parts[0]), Col.from(parts[1]));
            } catch ( IllegalArgumentException e ) {
                printAlert(helpMessage);
            }
        }
    }

    /**
     * Overloaded method to support PlayerToken instead of int
     * This method allows calling promptForPlayer using X or O directly
     * @param token PlayerToken (X or O)
     * @return Player object
     */
    public static Player promptForPlayer(PlayerToken token) {
        return switch (token) {
            case X -> promptForPlayer(1);
            case O -> promptForPlayer(2);
        };
    }
}
