package subcomponents.cell;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sheet.cell.Cell;

public class CellController {

    @FXML
    private Label cellLabel;  // התווית שמציגה את הערך של התא

    private Cell cell;  // ייצוג התא

    // פונקציה שמקבלת תא ומציגה את הערכים שלו
    public void setCell(Cell cell) {
        this.cell = cell;
        updateCellView();
    }

    // עדכון תצוגת התא עם ערך אפקטיבי
    private void updateCellView() {
        cellLabel.setText(cell.getEffectiveValue().toString());
    }
}
