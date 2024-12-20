package component.popup.viewonlysheet;

import component.selectedSheetView.main.SelectedSheetViewController;
import component.selectedSheetView.subcomponent.sheet.CellModel;
import component.selectedSheetView.subcomponent.sheet.CellStyle;
import component.selectedSheetView.subcomponent.sheet.UIModelSheet;
//import component.subcomponent.sheet.CellModel;
//import component.subcomponent.sheet.CellStyle;
//import component.subcomponent.sheet.UIModelSheet;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ViewOnlySheetController {

    @FXML private GridPane viewOnlySheetGridPane;
    private UIModelSheet uiModelSheet;


    private SelectedSheetViewController selectedSheetViewController;

    @FXML public void initialize() {uiModelSheet = new UIModelSheet();}


    public void setAppController(SelectedSheetViewController selectedSheetViewController) {
        this.selectedSheetViewController = selectedSheetViewController;
    }


    public void initViewOnlySheetAndBindToUIModel(DTOSheet dtoSheet, boolean includeVisuals) {

        viewOnlySheetGridPane.getChildren().clear();

        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int WidthOfCols = dtoSheet.getWidthOfColumns();
        int HeightOfRows = dtoSheet.getHeightOfRows();

        uiModelSheet.initializeModel(numOfRows+1, numOfCols+1, WidthOfCols, HeightOfRows);

        //check thr casting!
        if (includeVisuals) {
            UIModelSheet originalModel = selectedSheetViewController.getCurrentUIModel(); // המודל המקורי מהגיליון הראשי
            uiModelSheet = originalModel.copyModel();// יצירת עותק מהמודל המקורי
        }


        for (int col = 1; col <= numOfCols; col++) {

            String colLetter = String.valueOf((char) ('A' + col - 1));
            ;
            if (dtoSheet.getFirstColumnLetter() != '0') {
                colLetter = String.valueOf((char) (dtoSheet.getFirstColumnLetter() + col - 1));
            }
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

                int realCol = col;
                int realRow = row;
//                if(dtoSheet.getFirstColumnLetter() != '0'){
//                    realCol = dtoSheet.getFirstColumnLetter() -'A' + col;
//                }
//                if(dtoSheet.getFirstRow()!=1){
//                    realRow = dtoSheet.getFirstRow()+row-1;
//                }
                String cellKey = getCellId(realCol, realRow);
                Label cellLabel = new Label();

                DTOCell dtoCell = dtoSheet.getCells().get(cellKey);
                if (dtoCell != null) {
                    uiModelSheet.setCellValue(cellKey, dtoCell.getEffectiveValue().toString());

                    //TODO: UIMODEL

                   //*****************************************************************************************//
                    if (includeVisuals) {
                       String originalCellId = dtoCell.getCellId();  // מזהה התא המקורי
                        CellModel originalCellModel = selectedSheetViewController.getCurrentUIModel().getCell(originalCellId); // קבלת העיצוב מהמודל הראשי

                        if (originalCellModel != null) {
                            // Set the original cell's visuals (background, text color, font, etc.)
                            uiModelSheet.setCellBackgroundColor(cellKey, originalCellModel.backgroundColorProperty().get());
                            uiModelSheet.setCellTextColor(cellKey, originalCellModel.textColorProperty().get());
                            uiModelSheet.setCellFont(cellKey, originalCellModel.fontProperty().get());
                            uiModelSheet.setCellBorderColor(cellKey, CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue());
                            uiModelSheet.setCellBorderStyle(cellKey, CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue());
                            uiModelSheet.setCellBorderWidth(cellKey, CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue());

//                            String columnCellId = getCellId(0, getRealRowFromCellId(originalCellId));
//                            uiModelSheet.setCellHeight(columnCellId,originalCellModel.hightProperty().get());
//


//                            String columnCellId = getCellId(0, row);
//                            String nextColumnCellId = getCellId(0, getRealRowFromCellId(originalCellId));
//                            CellModel nextCellModel = appController.getCurrentUIModel().getCell(nextColumnCellId);
//                            uiModelSheet.setCellHeight(columnCellId,nextCellModel.hightProperty().get());
                        }
                    }

                }
                else{
                    uiModelSheet.setCellValue(cellKey, "");
                    uiModelSheet.setCellBackgroundColor(cellKey, CellStyle.NORMAL_CELL_BACKGROUND_COLOR.getColorValue());
                    uiModelSheet.setCellTextColor(cellKey, CellStyle.NORMAL_CELL_TEXT_COLOR.getColorValue());
                    // uiModelSheet.setCellFont(cellKey, CellStyle.NO);
                    uiModelSheet.setCellBorderColor(cellKey, CellStyle.NORMAL_CELL_BORDER_COLOR.getColorValue());
                    uiModelSheet.setCellBorderStyle(cellKey, CellStyle.NORMAL_CELL_BORDER_STYLE.getStyleValue());
                    uiModelSheet.setCellBorderWidth(cellKey, CellStyle.NORMAL_CELL_BORDER_WIDTH.getWidthValue());
                }
                //*****************************************************************************************//
                    uiModelSheet.bindCellToModel(cellLabel, cellKey);
                    viewOnlySheetGridPane.add(cellLabel, col, row);


            }
        }

    }


    public void displayViewOnlySheetByVersion( DTOSheet dtoSheet) {
       initViewOnlySheetAndBindToUIModel(dtoSheet,false);

    }


    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }


    private int getRealRowFromCellId(String cellId) {
        return Integer.parseInt(cellId.substring(2));
    }


}

