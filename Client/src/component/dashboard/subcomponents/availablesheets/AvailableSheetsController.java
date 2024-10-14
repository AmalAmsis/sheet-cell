package component.dashboard.subcomponents.availablesheets;

import component.dashboard.main.maindashboard.DashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

public class AvailableSheetsController {

    private DashboardController dashboardController;

    @FXML private VBox availableSheetTable;
    @FXML private VBox permissionsTable;


    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }

    public void addSheetToAvailableSheetTable(CheckBox checkBox) {
        availableSheetTable.getChildren().add(checkBox);

        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                for (var child : availableSheetTable.getChildren()) {
                    if (child instanceof CheckBox && child != checkBox) {
                        ((CheckBox) child).setSelected(false);
                    }
                }
            }
        });
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }
}
