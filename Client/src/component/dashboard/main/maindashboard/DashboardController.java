package component.dashboard.main.maindashboard;


import component.dashboard.subcomponents.availableSheets.AvailableSheetRow;
import component.dashboard.subcomponents.availableSheets.AvailableSheetsController;
import component.dashboard.subcomponents.command.CommandController;
import component.dashboard.subcomponents.header.DashboardHeaderController;
import component.main.SheetCellAppMainController;
import dto.DTOSheet;
import dto.DTOSheetInfo;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.Closeable;
import java.io.IOException;

public class DashboardController implements Closeable {

    private SheetCellAppMainController sheetCellAppMainController;

    @FXML private VBox dashboardHeader;
    @FXML private DashboardHeaderController dashboardHeaderController;
    @FXML private ScrollPane availableSheets;
    @FXML private AvailableSheetsController availableSheetsController;
    @FXML private ScrollPane command;
    @FXML private CommandController commandController;

    @FXML
    public void initialize() {
        // Ensure that the sub-controllers are set up properly
        if (dashboardHeaderController != null && availableSheetsController != null && commandController != null) {
            dashboardHeaderController.setDashboardController(this);
            availableSheetsController.setDashboardController(this);
            commandController.setDashboardController(this);
        }
    }


    public void setSheetCellAppMainController(SheetCellAppMainController sheetCellAppMainController) {
        this.sheetCellAppMainController = sheetCellAppMainController;
    }

    public void addSheetToAvailableSheets(AvailableSheetRow row) {
        availableSheetsController.addSheetToAvailableSheetTable(row);
    }


    public String getSelectedSheetName() {
        return availableSheetsController.getSelectedSheetName();
    }

    public void switchToSelectedSheetView(DTOSheet dtoSheet,String selectedSheetName , String permission) {
        sheetCellAppMainController.switchToSelectedSheetView(dtoSheet,selectedSheetName,permission);

    }

    //??????????????????????????????????
    @Override
    public void close() throws IOException {
        availableSheetsController.close();
    }

    public void createAvailableSheetRow(DTOSheetInfo dtoSheetInfo) {
        // Create a new row for the available sheet table
        AvailableSheetRow row = new AvailableSheetRow(dtoSheetInfo.getSheetName(),
                dtoSheetInfo.getNumRows() + "x" + dtoSheetInfo.getNumCols(),
                dtoSheetInfo.getUploadBy(),
                this.availableSheetsController);

    }


    public String getUploaderName(String sheetName) {
        for (AvailableSheetRow row : availableSheetsController.getAvailableSheetRows()) {
            if (row.getSheetName().equals(sheetName)) {
                return row.getUploadedBy();
            }
        }
        return null; //
    }

    public String getCurrentUsername() {
        return sheetCellAppMainController.getUserName();
    }
}
