package dto;

public class DTOPermissionRequest {

    private String Type;
    private String requestedBy;
    private String Status;
    private String newRequestType;
    private String newRequestStatus;


    public DTOPermissionRequest(String type, String requestedBy, String status, String newRequestType) {
            this.Type = type;
            this.requestedBy = requestedBy;
            this.Status = status;
            this.newRequestType = newRequestType;
            this.newRequestStatus = newRequestStatus;
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

    public String getNewRequestStatus() {
        return newRequestStatus;
    }

}
