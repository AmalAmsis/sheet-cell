package subcomponents.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sheet.SheetImpl;
import sheet.coordinate.Coordinate;
import sheet.cell.CellImpl;
import sheet.coordinate.CoordinateImpl;
import subcomponents.app.AppController;
import subcomponents.sheet.SheetController;

public class Test2 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and get the SheetController
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/subcomponents/sheet/sheet.fxml"));
        Parent root = loader.load();

        // Create a sample SheetImpl with dummy data
        SheetImpl sampleSheet = createSampleSheet();

        // Get the SheetController and call loadSheet with the sample sheet
        subcomponents.sheet.SheetController controller = loader.getController();

        System.out.println("Controller: " + controller);

        if (controller != null) {
            controller.loadSheet(sampleSheet);  // Check if loadSheet runs properly
        }
        // Set up the scene and stage
        primaryStage.setTitle("Dynamic Sheet Example");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    // Sample method to create a SheetImpl with dummy data
    private SheetImpl createSampleSheet() {
        // Dummy sheet with 5 rows, 5 columns, and default sizes
        int numRows = 5;
        int numCols = 5;
        int rowHeight = 30;
        int colWidth = 100;

        // Create the sheet
        SheetImpl sheet = new SheetImpl("Test Sheet", numRows, numCols, rowHeight, colWidth);

        // Create a few sample cells with effective values
        sheet.setCell(new CoordinateImpl('A', 1), "42");
        sheet.setCell(new CoordinateImpl('B', 2), "Hello");
        sheet.setCell(new CoordinateImpl('C', 3), "TRUE");
        sheet.setCell(new CoordinateImpl('D', 4), "3.14");
        sheet.setCell(new CoordinateImpl('E', 5), "Goodbye");

        return sheet;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
