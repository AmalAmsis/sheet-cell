package menu;

import manager.UIManager;

public enum Command {
    LOAD_FILE("1. Load spreadsheet data file") {
        @Override
        public void execute(UIManager manager) {
            loadFile(manager);
        }
    },
    SHOW_SHEET("2. Display the sheet") {
        @Override
        public void execute(UIManager manager) {
            showSheet(manager);
        }
    },
    SHOW_CELL("3. Display a single cell's value") {
        @Override
        public void execute(UIManager manager) {
            showCell(manager);
        }
    },
    UPDATE_CELL("4. Update a single cell's value") {
        @Override
        public void execute(UIManager manager) {
            updateCell(manager);
        }
    },
    SHOW_VERSIONS("5. Display versions") {
        @Override
        public void execute(UIManager manager) {
            showVersions(manager);
        }
    },
    EXIT("6. Exit the system") {
        @Override
        public void execute(UIManager manager) {
            exitSystem(manager);
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

    private static void loadFile(UIManager manager) {
        // Implement the logic to load the XML file
        System.out.println("Loading spreadsheet data file...");
    }

    private static void showSheet(UIManager manager) {
        // Implement the logic to display the sheet
        manager.displaySheet();
        System.out.println("Displaying the sheet...");
    }

    private static void showCell(UIManager manager) {
        // Implement the logic to display a single cell's value
        System.out.println("Displaying a single cell's value...");
    }

    private static void updateCell(UIManager manager) {
        // Implement the logic to update a single cell's value
        System.out.println("Updating a single cell's value...");
    }

    private static void showVersions(UIManager manager) {
        // Implement the logic to display the versions
        System.out.println("Displaying versions...");
    }

    private static void exitSystem(UIManager manager) {
        // Implement the logic to exit the system
        System.out.println("Exiting the system...");
    }
}
