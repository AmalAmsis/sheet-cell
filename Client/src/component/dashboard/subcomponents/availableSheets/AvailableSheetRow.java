package component.dashboard.subcomponents.availableSheets;

import javafx.scene.control.CheckBox;

public class AvailableSheetRow {

    private CheckBox selected;
    private String sheetName;
    private String sheetSize;
    private String uploadedBy;
    private AvailableSheetsController controller;  // Reference to the controller


    public AvailableSheetRow(String sheetName, String sheetSize, String uploadedBy, AvailableSheetsController controller) {
        this.selected = new CheckBox();
        this.sheetName = sheetName;
        this.sheetSize = sheetSize;
        this.uploadedBy = uploadedBy;
        this.controller = controller;


        // Logic to ensure only one checkbox is selected at a time
        selected.setOnAction(e -> {
            if (selected.isSelected()) {
                controller.handleCheckBoxSelection(selected);
            }
        });
    }


    public CheckBox getSelected() {
        return selected;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getSheetSize() {
        return sheetSize;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

}
