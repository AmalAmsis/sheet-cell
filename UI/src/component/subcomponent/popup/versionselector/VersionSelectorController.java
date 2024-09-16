package component.subcomponent.popup.versionselector;

import component.main.app.AppController;
import component.subcomponent.popup.viewonlysheet.ViewOnlySheetController;
import component.subcomponent.sheet.SheetController;
import dto.DTOSheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;


public class VersionSelectorController {

    private AppController appController;

    @FXML private Label numOfChangesInSelectedVersionLabel;
    @FXML private Label selectedVersionLabel;
    @FXML private MenuButton versionMenuBar;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    @FXML
    void clickMeCancelButtom(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void clickMeOKButtom(ActionEvent event) {
        int version = Integer.parseInt(selectedVersionLabel.getText());
        loadVersion(version);
    }

    public void loadVersion(int version) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/popup/viewonlysheet/viewOnlySheet.fxml"));
            Parent root = loader.load();

            ViewOnlySheetController viewOnlySheetController = loader.getController();
            viewOnlySheetController.setAppController(appController);
            viewOnlySheetController.displayViewOnlySheetByVersion(version);

            Stage stage = new Stage();
            stage.setTitle("Version " + version);
            stage.setScene(new Scene(root));
            stage.show();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    public void loadVersion(int version) {
//        try{
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/component/subcomponent/sheet/sheet.fxml"));
//            Parent root = loader.load();
//
//            SheetController sheetController = loader.getController();
//            sheetController.setAppController(appController);
//            sheetController.displaySheetByVersion(version);
//
//            Stage stage = new Stage();
//            stage.setTitle("Version " + version);
//            stage.setScene(new Scene(root));
//            stage.show();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    public void loadVersionToMenuBar() {

        int numOfVersion = appController.getNumOfVersions();
        versionMenuBar.getItems().clear();
        for (int i = 1; i < numOfVersion; i++) {

            MenuItem versionItem = new MenuItem("Version " + i);

            int version = i;
            versionItem.setOnAction(e -> {setVersionData(version);});
            versionMenuBar.getItems().add(versionItem);
        }
    }

    public void setVersionData(int version){
        int numOfChanges = appController.getNumOfChangesInVersion(version);
        numOfChangesInSelectedVersionLabel.setText(String.valueOf(numOfChanges));
        selectedVersionLabel.setText(String.valueOf(version));
        numOfChangesInSelectedVersionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1abc9c; -fx-font-size: 14px;");
        selectedVersionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1abc9c; -fx-font-size: 14px;");
    }



}
