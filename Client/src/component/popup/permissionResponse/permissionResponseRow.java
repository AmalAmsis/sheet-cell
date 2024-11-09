package component.popup.permissionResponse;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;

public class permissionResponseRow {
    private final StringProperty userName;
    private final StringProperty permissionType;
    private final ObjectProperty<MenuButton> approvalOptions;

    private static final String APPROVED = "APPROVED";
    private static final String DENIED = "DENIED";

    public permissionResponseRow(String userName, String permissionType, MenuButton approvalOptions) {
        this.userName = new SimpleStringProperty(userName);
        this.permissionType = new SimpleStringProperty(permissionType);
        this.approvalOptions = new SimpleObjectProperty<>(approvalOptions);
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public StringProperty permissionTypeProperty() {
        return permissionType;
    }

    public ObjectProperty<MenuButton> approvalOptionsProperty() {
        return approvalOptions;
    }


    public MenuButton getApprovalOptions() {
        return approvalOptions.get();
    }

    public String getUserName() {
        return userName.get();
    }

    public String getPermissionType(){
        return permissionType.get();
    }
}
