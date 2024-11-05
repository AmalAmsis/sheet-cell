package permission.sheetPermission;

import java.util.HashMap;
import java.util.Map;

public class SheetPermissionImpl implements SheetPermission {

    private static final String NONE = "NONE";
    private static final String READER = "READER";
    private static final String WRITER = "WRITER";
    private static final String OWNER = "OWNER";

    private String owner;
    Map<String,String> sheetPermissions;

    public SheetPermissionImpl(String owner) {
        sheetPermissions = new HashMap<String,String>();
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void addUserPermission(String userName, String permission) {
        sheetPermissions.put(userName, permission);
    }

    public String getUserPermission(String userName) {
        return sheetPermissions.get(userName);
    }

    public void removeUserPermission(String userName) {
        sheetPermissions.remove(userName);
    }

    public void changeUserPermissions(String userName, String permission) {
        sheetPermissions.put(userName, permission);
    }

}
