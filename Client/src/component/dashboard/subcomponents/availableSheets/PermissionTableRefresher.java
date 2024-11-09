package component.dashboard.subcomponents.availableSheets;


import java.util.TimerTask;

public class PermissionTableRefresher extends TimerTask {

    private AvailableSheetsController availableSheetsController;
    private String sheetName;

    public PermissionTableRefresher(AvailableSheetsController controller, String sheetName) {
        this.availableSheetsController = controller;
        this.sheetName = sheetName;
    }

    @Override
    public void run() {
        // Calling the request to get the updated permissions for the selected sheet
        availableSheetsController.sendSheetPermissionRequest(sheetName);
    }
}