package component.dashboard.subcomponents.availableSheets;

public class PermissionRow {
    private String userName;
    private String permissionType;
    private String approvedByOwner;

    public PermissionRow(String userName, String permissionType, String approvedByOwner) {
        this.userName = userName;
        this.permissionType = permissionType;
        this.approvedByOwner = approvedByOwner;
    }

    public String getUserName() {
        return userName;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public String getApprovedByOwner() {
        return approvedByOwner;
    }
}

