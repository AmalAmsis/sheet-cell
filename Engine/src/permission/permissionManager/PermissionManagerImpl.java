package permission.permissionManager;

import permission.sheetPermission.SheetPermission;

import java.util.HashMap;
import java.util.Map;

public class PermissionManagerImpl implements PermissionManager {

    private Map<String, SheetPermission> permissions;

    public PermissionManagerImpl(String userName) {
        permissions = new HashMap<>();
    }

    @Override public void addSheetPermission(String sheetName, SheetPermission sheetPermission) {
        permissions.put(sheetName, sheetPermission);
    }

    public SheetPermission getSheetPermission(String sheetName) {
        return permissions.get(sheetName);
    }




}
