package manager;

import menu.Command;

public interface UIManager {

    //Amal
    Command printMenuAddGetUserChoice();
    //yarden
    void displaySheet();    //yarden
    void displayCell();
    //yarden
    void loadXmlFile(String filePath);
    void loadSeralizationFile();
    void saveSeralizationFile();
    void loadXmlFileFromUser();

}
