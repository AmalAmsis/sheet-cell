package permission.sheetPermission;

import java.util.Map;

public interface SheetPermission {

    Map<String,PermissionRequest> getSheetPermissions();

    String getOwner();

    PermissionRequest getUserPermission(String userName);

    String getNewRequestType(String userName);
}
