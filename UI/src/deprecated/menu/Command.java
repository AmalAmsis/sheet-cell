package deprecated.menu;

import deprecated.manager.UIManager;

/**
 * The Command enum represents various commands that the user can execute.
 * Each command has a description and an abstract execute method that must be implemented by each enum constant.
 * The commands include operations such as loading data, displaying the sheet, updating a cell, and saving the system state.
 */
public enum Command {

    LOAD_XML_FILE("Load spreadsheet data from xml file") {
        @Override
        public void execute(UIManager manager) {
            manager.loadXmlFileFromUser();
        }
    },
    SHOW_SHEET("Display the sheet") {
        @Override
        public void execute(UIManager manager) {
            manager.displaySheet();
        }
    },
    SHOW_CELL("Display a single cell's value") {
        @Override
        public void execute(UIManager manager) {manager.displayCell();}
    },
    UPDATE_CELL("Update a single cell's value") {
        @Override
        public void execute(UIManager manager) {
            manager.updateCell();
        }
    },
    SHOW_VERSIONS("Display versions") {
        @Override
        public void execute(UIManager manager) {
            manager.displaySheetVersion();
        }
    },

    SAVE_SYSTEM_STATE("Save system state") {
        @Override
        public void execute(UIManager manager) {
            manager.saveSystemState();
        }
    },

    LOAD_SYSTEM_STATE("Load system state") {
        @Override
        public void execute(UIManager manager) {
            manager.loadSystemState();
        }
    },

    EXIT("Exit the system") {
        @Override
        public void execute(UIManager manager) {
        }
    };

    private final String description;

    Command(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(UIManager manager);

}
