package manager;

import menu.Command;

public interface UIManager {

    //Amal
    Command printMenuAddGetUserChoice();
    //yarden
    void displaySheet();    //yarden
    void displayCell();
    void updateCell();
    //yarden
    void loadSystemState();
    void saveSystemState();
    void loadXmlFileFromUser();
    void displaySheetVersion();

}
