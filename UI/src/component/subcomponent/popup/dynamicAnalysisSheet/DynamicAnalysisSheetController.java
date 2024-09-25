package component.subcomponent.popup.dynamicAnalysisSheet;


import component.main.app.AppController;
import component.subcomponent.sheet.CellModel;
import component.subcomponent.sheet.CellStyle;
import component.subcomponent.sheet.UIModelSheet;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

public class DynamicAnalysisSheetController {

    @FXML private GridPane dynamicAnalysisSheetGridPane;
    @FXML private Slider valueSlider;

    private AppController appController;
    private UIModelSheet uiModelSheet;

    private String selectedCellKey; // תא שנבחר לניתוח
    private double minValue;
    private double maxValue;
    private double stepSize;

    public void initialize() {
        uiModelSheet = new UIModelSheet();

    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void initDynamicAnalysisSheet(DTOSheet dtoSheet, String cellKey, double min, double max, double step) {
        this.selectedCellKey = cellKey;
        this.minValue = min;
        this.maxValue = max;
        this.stepSize = step;

        // Configure the slider
        valueSlider.setMin(minValue);
        valueSlider.setMax(maxValue);
        valueSlider.setBlockIncrement(stepSize);


        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int WidthOfCols = dtoSheet.getWidthOfColumns();
        int HeightOfRows = dtoSheet.getHeightOfRows();

        uiModelSheet.initializeModel(numOfRows+1, numOfCols+1, WidthOfCols, HeightOfRows);

        dynamicAnalysisSheetGridPane.getChildren().clear();
        UIModelSheet originalModel = appController.getCurrentUIModel(); // המודל המקורי מהגיליון הראשי
        uiModelSheet = originalModel.copyModel();// יצירת עותק מהמודל המקורי

        for (int col = 1; col <= numOfCols; col++) {

            String cellId = getCellId(col, 0);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(WidthOfCols, 15);
            uiModelSheet.bindCellToModel(cellLabel, cellId);
            dynamicAnalysisSheetGridPane.add(cellLabel, col, 0);
        }

        for (int row = 1; row <= numOfRows; row++) {
            String cellId = getCellId(0, row);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(25, HeightOfRows);
            uiModelSheet.bindCellToModel(cellLabel, cellId);
            dynamicAnalysisSheetGridPane.add(cellLabel, 0, row);

        }

        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {
                String cellId = getCellId(col, row);
                Label cellLabel = new Label();
                uiModelSheet.bindCellToModel(cellLabel, cellId);
                dynamicAnalysisSheetGridPane.add(cellLabel, col, row);
            }
        }

        // Setup slider change listener
        // Setup slider change listener
        valueSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double steppedValue = snapToStep(newVal.doubleValue(), minValue, stepSize);
            valueSlider.setValue(steppedValue); // Update slider to the stepped value
            onSliderValueChanged(steppedValue); // Update cell value
        });


    }

    private double snapToStep(double value, double min, double step) {
        double adjustedValue = Math.round((value - min) / step) * step + min;
        return Math.max(min, Math.min(adjustedValue, maxValue)); // Ensure the value stays within range
    }


    private void onSliderValueChanged(double newValue) {
            DTOSheet sheet = appController.updateTemporaryCellValue(selectedCellKey, newValue);
            for (int row = 1; row <= sheet.getNumOfRows(); row++) {
                for (int col = 1; col <= sheet.getNumOfColumns(); col++) {
                    String cellId = getCellId(col, row);
                    DTOCell dtoCell = sheet.getCells().get(cellId);
                    if (dtoCell != null) {
                        uiModelSheet.setCellValue(cellId, dtoCell.getEffectiveValue().toString());
                    }
                }
            }

    }



    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1));
        return String.valueOf(colLetter) + ":" + row;
    }
}


