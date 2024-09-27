package component.subcomponent.popup.graph;

import component.main.app.AppController;
import component.subcomponent.popup.errormessage.ErrorMessage;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.List;

public class GraphController {

    private AppController appController;

    @FXML private TextField xAxisRangeField;
    @FXML private TextField yAxisRangeField;
    @FXML private ChoiceBox<String> graphTypeChoiceBox;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    private void initialize() {
        graphTypeChoiceBox.getItems().addAll("Line Chart", "Bar Chart");
        graphTypeChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    private void generateGraph() {
        try {
            String xRange = xAxisRangeField.getText();
            String yRange = yAxisRangeField.getText();

            List<String> xData = appController.getColumnValuesInRange(xRange);
            List<String > yData = appController.getColumnValuesInRange(yRange);

            if (xData.size() != yData.size()) {
                throw new IllegalArgumentException("The number of values in the Y-axis range does not match the number of values in the X-axis range. Please ensure both ranges contain the same number of cells.");

            }

            String selectedGraphType = graphTypeChoiceBox.getValue();

            if ("Line Chart".equals(selectedGraphType)) {
                displayLineChart(xData, yData);
            } else {
                displayBarChart(xData, yData);
            }
        }catch (Exception e){
            new ErrorMessage(e.getMessage());
        }

    }

    private void displayLineChart(List<String> xData, List<String> yData) {
        Stage graphStage = new Stage();
        LineChart<String, Number> lineChart = new LineChart<>(new CategoryAxis(), new NumberAxis());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < xData.size(); i++) {
            series.getData().add(new XYChart.Data<>(xData.get(i), Double.parseDouble(yData.get(i))));
        }

        lineChart.getData().add(series);
        graphStage.setTitle("Line Chart");
        graphStage.setScene(new Scene(lineChart, 800, 600));
        graphStage.show();
    }

    private void displayBarChart(List<String> xData, List<String> yData) {
        Stage graphStage = new Stage();
        BarChart<String, Number> barChart = new BarChart<>(new CategoryAxis(), new NumberAxis());

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < xData.size(); i++) {
            series.getData().add(new XYChart.Data<>(xData.get(i), Double.parseDouble(yData.get(i))));
        }

        barChart.getData().add(series);
        graphStage.setTitle("Bar Chart");
        graphStage.setScene(new Scene(barChart, 800, 600));
        graphStage.show();
    }
}

