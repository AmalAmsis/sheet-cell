package component.main;

import component.login.LoginController;
import component.selectedSheetView.main.SelectedSheetViewController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

import static util.Constants.*;

public class SheetCellAppMainController implements Closeable {

    @FXML private AnchorPane mainPanel;
    @FXML private Label userNameLabel;

    private GridPane loginComponent;
    private LoginController loginComponentController;

    private GridPane selectedSheetViewComponent;
    private SelectedSheetViewController selectedSheetViewComponentController;

    @Override
    public void close() throws IOException {

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

    }

    public void updateUserName(String userName) {
        currentUserName.set(userName);
    }

    private void setMainPanelTo(Parent pane) {
        mainPanel.getChildren().clear();
        mainPanel.getChildren().add(pane);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
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

}
