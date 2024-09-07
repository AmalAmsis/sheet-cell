package subcomponents.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sheet.SheetImpl;
import sheet.coordinate.CoordinateImpl;
import subcomponents.app.AppController;

public class Test3 extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main FXML file (app.fxml)
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/subcomponents/app/app.fxml"));
        Parent root = loader.load();

        // Create a sample SheetImpl with dummy data
        SheetImpl sampleSheet = createSampleSheet();

        // Get the AppController and load the sheet into the UI
        AppController appController = loader.getController();
        appController.loadSheet(sampleSheet);

        // Set up the scene and stage
        Scene scene = new Scene(root);
        primaryStage.setTitle("Dynamic Spreadsheet with Header");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create a sample SheetImpl with some dummy data
    private SheetImpl createSampleSheet() {
        int numRows = 5;
        int numCols = 5;
        int rowHeight = 30;
        int colWidth = 100;

        // Create the sheet with the specified number of rows, columns, and sizes
        SheetImpl sheet = new SheetImpl("Test Sheet", numRows, numCols, rowHeight, colWidth);

        // Set some sample cells with dummy values
        sheet.setCell(new CoordinateImpl('A', 1), "42");
        sheet.setCell(new CoordinateImpl('B', 2), "Hello");
        sheet.setCell(new CoordinateImpl('C', 3), "TRUE");
        sheet.setCell(new CoordinateImpl('D', 4), "3.14");
        sheet.setCell(new CoordinateImpl('E', 5), "Goodbye");

        return sheet;
    }

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }
}
