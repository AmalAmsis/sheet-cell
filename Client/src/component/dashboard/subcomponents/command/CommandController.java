package component.dashboard.subcomponents.command;

import component.dashboard.subcomponents.maindashboard.DashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;

public class CommandController {

    private DashboardController dashboardController;

    @FXML private Button ackOrDenyPermissionRequestButton;
    @FXML private Button requestPermissionButton;
    @FXML private Button viewSheetButton;

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public DashboardController getDashboardController() {
        return dashboardController;
    }
}
