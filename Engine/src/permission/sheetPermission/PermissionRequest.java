package permission.sheetPermission;

public class PermissionRequest {

    private String Type;
    private String requestedBy;
    private String Status;
    private String newRequestType;


    public PermissionRequest(String type, String requestedBy, String status) {
        this.Type = type;
        this.requestedBy = requestedBy;
        this.Status = status;
        this.newRequestType = null;
    }

    public String getType() {
        return Type;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public String getStatus() {
        return Status;
    }

    public String getNewRequestType() {
        return newRequestType;
    }

    public void setNewRequestType(String newRequestType) {
        this.newRequestType = newRequestType;
    }

}
