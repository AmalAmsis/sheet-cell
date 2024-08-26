package menu;

public enum Command {
    LOAD_FILE("1. Load spreadsheet data file") {
        @Override
        public void execute() {
            loadFile();
        }
    },
    SHOW_SHEET("2. Display the sheet") {
        @Override
        public void execute() {
            showSheet();
        }
    },
    SHOW_CELL("3. Display a single cell's value") {
        @Override
        public void execute() {
            showCell();
        }
    },
    UPDATE_CELL("4. Update a single cell's value") {
        @Override
        public void execute() {
            updateCell();
        }
    },
    SHOW_VERSIONS("5. Display versions") {
        @Override
        public void execute() {
            showVersions();
        }
    },
    EXIT("6. Exit the system") {
        @Override
        public void execute() {
            exitSystem();
        }
    };

    private final String description;

    Command(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute();

    private static void loadFile() {
        // Implement the logic to load the XML file
        System.out.println("Loading spreadsheet data file...");
    }

    private static void showSheet() {
        // Implement the logic to display the sheet
        System.out.println("Displaying the sheet...");
    }

    private static void showCell() {
        // Implement the logic to display a single cell's value
        System.out.println("Displaying a single cell's value...");
    }

    private static void updateCell() {
        // Implement the logic to update a single cell's value
        System.out.println("Updating a single cell's value...");
    }

    private static void showVersions() {
        // Implement the logic to display the versions
        System.out.println("Displaying versions...");
    }

    private static void exitSystem() {
        // Implement the logic to exit the system
        System.out.println("Exiting the system...");
    }
}
