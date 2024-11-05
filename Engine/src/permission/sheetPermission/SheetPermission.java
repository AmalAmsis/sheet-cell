package permission.sheetPermission;

import java.util.Map;

public interface SheetPermission {

    Map<String,PermissionRequest> getSheetPermissions();

    String getOwner();
}
