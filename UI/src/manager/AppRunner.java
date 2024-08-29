package manager;

import menu.Command;
import static menu.Command.EXIT;


/**
 * AppRunner is the entry point for the application.
 * This class handles the main loop where the user interacts with the application via a menu.
 * Based on the user's input, it executes various commands until the user chooses to exit.
 */
public class AppRunner {


    /**
     * The main method that serves as the entry point of the application.
     * It initializes the UIManager, presents the menu to the user, and handles the command execution loop.
     * @param args Command line arguments (not used in this application).
     */
    public static void main(String[] args) {

        UIManager manager = new UIManagerImpl();// Initialize the UI manager.

        boolean exit = false; // A flag to determine when to exit the application.

        while(!exit) {
            Command command = manager.printMenuAddGetUserChoice(); // Display menu and get user choice.
            if(command != EXIT) { // If the user didn't choose to exit,
                command.execute(manager);  // execute the selected command.
            }
            else{
                exit = true; // Set the flag to true to exit the loop and end the application.
            }
        }
    }
}
