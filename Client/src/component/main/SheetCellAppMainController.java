package component.main;

import component.dashboard.main.maindashboard.DashboardController;
import component.login.LoginController;
import component.selectedSheetView.main.SelectedSheetViewController;
import dto.DTOSheet;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static util.Constants.*;

public class SheetCellAppMainController implements Closeable {

    private Stage primaryStage;

    @FXML private AnchorPane mainPanel;
    @FXML private Label userNameLabel;

    private GridPane loginComponent;
    private LoginController loginComponentController;


    private BorderPane selectedSheetViewComponent;
    private SelectedSheetViewController selectedSheetViewComponentController;

    private BorderPane dashboardComponent;
    private DashboardController dashboardComponentController;


    //?????????????????????????????????????????????????
    @Override
    public void close() throws IOException {
        if (selectedSheetViewComponentController != null) {
            selectedSheetViewComponentController.close();
        }
        if (dashboardComponentController != null) {
            dashboardComponentController.close();
        }
    }


    private final StringProperty currentUserName;

    public SheetCellAppMainController() {
        currentUserName = new SimpleStringProperty(NO_USERNAME);
    }

    @FXML
    public void initialize() {
        userNameLabel.textProperty().bind(Bindings.concat("Hello ", currentUserName));

        // prepare components
        loadLoginPage();
        loadSelectedSheetViewPage();
        loadDashboardPage();

    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);

        AnchorPane.setTopAnchor(mainPanel,0.0 );
        AnchorPane.setBottomAnchor(mainPanel, 0.0);
        AnchorPane.setLeftAnchor(mainPanel, 0.0);
        AnchorPane.setRightAnchor(mainPanel, 0.0);
    }

    private void loadLoginPage() {
        URL loginPageUrl = getClass().getResource(LOGIN_PAGE_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(loginPageUrl);
            loginComponent = fxmlLoader.load();
            loginComponentController = fxmlLoader.getController();
            loginComponentController.setSheetCellAppMainController(this);
            setMainPanelTo(loginComponent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSelectedSheetViewPage(){
        URL selectedSheetViewPageUrl = getClass().getResource(SELECTED_SHEET_VIEW_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(selectedSheetViewPageUrl);
            selectedSheetViewComponent = fxmlLoader.load();
            selectedSheetViewComponentController = fxmlLoader.getController();
            selectedSheetViewComponentController.setSheetCellAppMainController(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void loadDashboardPage(){
        URL dashboardPageUrl = getClass().getResource(DASHBOARD_FXML_RESOURCE_LOCATION);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(dashboardPageUrl);
            dashboardComponent = fxmlLoader.load();
            dashboardComponentController = fxmlLoader.getController();
            dashboardComponentController.setSheetCellAppMainController(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void switchToSelectedSheetView(DTOSheet dtoSheet,String selectedSheetName){
        setMainPanelTo(selectedSheetViewComponent);
        selectedSheetViewComponentController.displaySheet(dtoSheet,selectedSheetName);
        selectedSheetViewComponentController.updateChoiceBoxes();
        selectedSheetViewComponentController.startRangePolling();
        selectedSheetViewComponentController.startSheetPolling();



        // לא סיימנו צריך להוסיף דברים בהמשך
    }

    public void switchToDashboard(){
        setMainPanelTo(dashboardComponent);
        selectedSheetViewComponentController.stopSheetPolling();
        selectedSheetViewComponentController.stopRangePolling();
        //לא סיימנו צריך להוסיף דברים בהמשך
    }

    /**
     * Applies a theme (stylesheet) to the application.
     * @param theme the name of the theme to apply.
     */
    public void applyTheme(String theme) {
        Scene scene = primaryStage.getScene();
        scene.getStylesheets().clear();

        if ("Style 1".equals(theme)) {

            String style1Css = getClass().getResource("/component/main/style1.css").toExternalForm();
            scene.getStylesheets().add(style1Css);
        } else {
            String style2Css = getClass().getResource("/component/main/style2.css").toExternalForm();
            scene.getStylesheets().add(style2Css);
            String singleCellCssFile = getClass().getResource("/component/selectedSheetView/subcomponent/sheet/single-cell.css").toExternalForm();
            scene.getStylesheets().add(singleCellCssFile);
        }
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public String getUserName(){
        return currentUserName.get();
    }
}
