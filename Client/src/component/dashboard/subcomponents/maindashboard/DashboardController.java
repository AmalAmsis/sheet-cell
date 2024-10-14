package component.dashboard.subcomponents.maindashboard;

import component.dashboard.subcomponents.availablesheets.AvailableSheetsController;
import component.dashboard.subcomponents.command.CommandController;
import component.dashboard.subcomponents.header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    @FXML private VBox header;
    @FXML private HeaderController headerController;
    @FXML private ScrollPane availableSheets;
    @FXML private AvailableSheetsController availableSheetsController;
    @FXML private ScrollPane commands;
    @FXML private CommandController commandController;

    @FXML
    public void initialize() {
        // Ensure that the sub-controllers are set up properly
        if (headerController != null && availableSheetsController != null && commandController != null) {
            headerController.setDashboardController(this);
            availableSheetsController.setDashboardController(this);
            commandController.setDashboardController(this);
        }
    }

    public void addSheetToAvailableSheets(CheckBox checkBox) {
        availableSheetsController.addSheetToAvailableSheetTable(checkBox);
    }
}
