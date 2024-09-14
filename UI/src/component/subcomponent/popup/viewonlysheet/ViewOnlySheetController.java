package component.subcomponent.popup.viewonlysheet;

import component.main.app.AppController;
import dto.DTOCell;
import dto.DTOSheet;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ViewOnlySheetController {

    @FXML private GridPane viewOnlySheetGridPane;

    AppController appController;
    UIModelViewOnlySheet uiModelViewOnlySheet;

    public void initialize() {
        uiModelViewOnlySheet = new UIModelViewOnlySheet();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }


    public void initViewOnlySheetAndBindToUIModel(DTOSheet dtoSheet) {
        viewOnlySheetGridPane.getChildren().clear();

        int numOfRows = dtoSheet.getNumOfRows();
        int numOfCols = dtoSheet.getNumOfColumns();
        int WidthOfCols = dtoSheet.getWidthOfColumns();
        int HeightOfRows = dtoSheet.getHeightOfRows();

        uiModelViewOnlySheet.initializeModel(numOfRows+1, numOfCols+1);

        for (int col = 1; col <= numOfCols; col++) {
            String colLetter = String.valueOf((char) ('A' + col - 1));
            String cellKey = getCellId(col, 0);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(WidthOfCols, 15);
            uiModelViewOnlySheet.setCellValue(cellKey, colLetter);
            uiModelViewOnlySheet.bindCellToModel(cellLabel, cellKey);
            viewOnlySheetGridPane.add(cellLabel, col, 0);
        }

        for (int row = 1; row <= numOfRows; row++) {
            String cellKey = getCellId(0, row);
            Label cellLabel = new Label();
            cellLabel.setPrefSize(25, HeightOfRows);
            uiModelViewOnlySheet.setCellValue(cellKey, String.valueOf(row));
            uiModelViewOnlySheet.bindCellToModel(cellLabel, cellKey);
            viewOnlySheetGridPane.add(cellLabel, 0, row);
        }

        for (int row = 1; row <= numOfRows; row++) {
            for (int col = 1; col <= numOfCols; col++) {

                String cellKey = getCellId(col, row);
                Label cellLabel = new Label();
                cellLabel.setPrefSize(WidthOfCols, HeightOfRows);


                DTOCell dtoCell = dtoSheet.getCells().get(cellKey);
                if (dtoCell != null) {
                    uiModelViewOnlySheet.setCellValue(cellKey, dtoCell.getEffectiveValue().toString());
                }

                uiModelViewOnlySheet.bindCellToModel(cellLabel, cellKey);
                viewOnlySheetGridPane.add(cellLabel, col, row);

            }
        }
    }

    public void displayViewOnlySheetByVersion(int version) {
        DTOSheet dtoSheet = appController.getSheetByVersion(version);
        initViewOnlySheetAndBindToUIModel(dtoSheet);

    }


    private String getCellId(int col, int row) {
        char colLetter = (char) ('A' + (col - 1)); // ממיר מספר עמודה לאות, לדוגמה 1 -> A
        return String.valueOf(colLetter) + ":" + row;
    }
}
