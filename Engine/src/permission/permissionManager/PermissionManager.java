package permission.permissionManager;

import permission.sheetPermission.SheetPermission;

public interface PermissionManager {

    void addSheetPermission(String sheetName, SheetPermission sheetPermission);

    SheetPermission  getSheetPermissions(String sheetName);
}
