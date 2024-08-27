package manager;

import menu.Command;

import static menu.Command.EXIT;

public class AppRunner {
    public static void main(String[] args) {
        UIManager manager = new UIManagerImpl();

        boolean exit = false;
        while(!exit) {
            Command command = manager.printMenuAddGetUserChoice();
            if(command != EXIT) {
                command.execute(manager);
            }
            else{
                exit = true;
            }
        }
    }
}
