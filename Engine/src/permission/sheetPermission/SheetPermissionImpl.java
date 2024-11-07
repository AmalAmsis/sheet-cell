package permission.sheetPermission;

import java.util.HashMap;
import java.util.Map;

public class SheetPermissionImpl implements SheetPermission {

    private static final String NONE = "NONE";
    private static final String READER = "READER";
    private static final String WRITER = "WRITER";
    private static final String OWNER = "OWNER";

    private String owner;
    Map<String,PermissionRequest> sheetPermissions;

    public SheetPermissionImpl(String owner) {
        sheetPermissions = new HashMap<String,PermissionRequest>();
        this.owner = owner;
    }

    @Override public String getOwner() {
        return owner;
    }

    public void addUserPermission(String userName, PermissionRequest permission) {
        sheetPermissions.put(userName, permission);
    }

    public PermissionRequest getUserPermission(String userName) {
        return sheetPermissions.get(userName);
    }

    public void removeUserPermission(String userName) {
        sheetPermissions.remove(userName);
    }

    public void changeUserPermissions(String userName, PermissionRequest permission) {
        sheetPermissions.put(userName, permission);
    }

    @Override
    public Map<String,PermissionRequest> getSheetPermissions(){
        return sheetPermissions;
    }

    @Override public String getNewRequestType(String userName){
        return sheetPermissions.get(userName).getNewRequestType();
    }

}
