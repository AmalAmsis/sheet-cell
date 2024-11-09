package permission.permissionManager;

import permission.sheetPermission.SheetPermission;

import java.util.HashMap;
import java.util.Map;

public class PermissionManagerImpl implements PermissionManager {

    private Map<String, SheetPermission> permissions;

    public PermissionManagerImpl() {
        permissions = new HashMap<>();
    }

    @Override public void addSheetPermission(String sheetName, SheetPermission sheetPermission) {
        permissions.put(sheetName, sheetPermission);
    }

    @Override public SheetPermission  getSheetPermissions(String sheetName){
        return permissions.get(sheetName);
    }




}
