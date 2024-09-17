package component.subcomponent.popup.viewonlysheet;

import component.main.app.AppController;
import component.subcomponent.sheet.CellModel;
import component.subcomponent.sheet.CellStyle;
import component.subcomponent.sheet.UIModelSheet;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ViewOnlySheetController {

    @FXML private GridPane viewOnlySheetGridPane;

    AppController appController;
    UIModelSheet uiModelSheet;

    public void initialize() {
        uiModelSheet = new UIModelSheet();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }




    public void initViewOnlySheetAndBindToUIModel(DTOSheet dtoSheet,boolean includeVisuals) {

        viewOnlySheetGridPane.getChildren().clear();

        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int WidthOfCols = dtoSheet.getWidthOfColumns();
        int HeightOfRows = dtoSheet.getHeightOfRows();

        uiModelSheet.initializeModel(numOfRows+1, numOfCols+1, WidthOfCols, HeightOfRows);


        //check thr casting!
        if (includeVisuals) {
            UIModelSheet originalModel = appController.getCurrentUIModel(); // המודל המקורי מהגיליון הראשי
            uiModelSheet = originalModel.copyModel(); // יצירת עותק מהמודל המקורי
        }

        for (int col = 1; col <= numOfCols; col++) {
            String colLetter = String.valueOf((char) ('A' + col - 1));
            String cellKey = getCellId(col, 0);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(WidthOfCols, 15);
            uiModelSheet.setCellValue(cellKey, colLetter);
            uiModelSheet.bindCellToModel(cellLabel, cellKey);
            viewOnlySheetGridPane.add(cellLabel, col, 0);
        }

        for (int row = 1; row <= numOfRows; row++) {
            String cellKey = getCellId(0, row);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(25, HeightOfRows);
            uiModelSheet.setCellValue(cellKey, String.valueOf(row));
            uiModelSheet.bindCellToModel(cellLabel, cellKey);
            viewOnlySheetGridPane.add(cellLabel, 0, row);
        }

        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {

                String cellKey = getCellId(col, row);
                Label cellLabel = new Label();

                DTOCell dtoCell = dtoSheet.getCells().get(cellKey);
                if (dtoCell != null) {
                    uiModelSheet.setCellValue(cellKey, dtoCell.getEffectiveValue().toString());

                    //*****************************************************************************************//
                    if (includeVisuals) {
                        String originalCellId = dtoCell.getCellId();  // מזהה התא המקורי
                        CellModel originalCellModel = appController.getCurrentUIModel().getCell(originalCellId); // קבלת העיצוב מהמודל הראשי

                        if (originalCellModel != null) {
                            // Set the original cell's visuals (background, text color, font, etc.)
                            uiModelSheet.setCellBackgroundColor(cellKey, originalCellModel.backgroundColorProperty().get());
                            uiModelSheet.setCellTextColor(cellKey, originalCellModel.textColorProperty().get());
                            uiModelSheet.setCellFont(cellKey, originalCellModel.fontProperty().get());
                            uiModelSheet.setCellBorderColor(cellKey, CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue());
                            uiModelSheet.setCellBorderStyle(cellKey, CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue());
                            uiModelSheet.setCellBorderWidth(cellKey, CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue());

                        }
                    }
                }

                //*****************************************************************************************//
                uiModelSheet.bindCellToModel(cellLabel, cellKey);
                viewOnlySheetGridPane.add(cellLabel, col, row);

            }
        }
    }

    public void displayViewOnlySheetByVersion(int version) {
        DTOSheet dtoSheet = appController.getSheetByVersion(version);
        initViewOnlySheetAndBindToUIModel(dtoSheet,false);

    }


    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }
}
