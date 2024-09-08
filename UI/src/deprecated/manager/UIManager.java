package deprecated.manager;

import deprecated.menu.Command;

/**
 * UIManager defines the interface for managing user interactions with the system.
 * This interface provides methods for displaying and updating the spreadsheet,
 * handling user inputs, and managing the system's state.
 * */
public interface UIManager {

    /**
     * Displays the main menu, prompts the user for a choice, and returns the selected command.
     * @return a Command object representing the user's choice.*/
    Command printMenuAddGetUserChoice();

    /**
     * Displays the entire spreadsheet to the user.*/
    void displaySheet();

    /**
     * Displays a specific cell's information to the user.*/
    void displayCell();

    /**
     * Prompts the user to update the value of a specific cell.*/
    void updateCell();

    /**
     * Loads the system state from a file, allowing the user to restore a previously saved state.*/
    void loadSystemState();

    /**
     * Saves the current system state to a file, allowing the user to preserve the current progress.*/
    void saveSystemState();

    /**
     * Prompts the user to load an XML file, typically used to load data into the system.*/
    void loadXmlFileFromUser();

    /**
     * Displays the version history of the spreadsheet, allowing the user to view or select previous versions.*/
    void displaySheetVersion();

}
