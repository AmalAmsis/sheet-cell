package dto;

public class DTOPermissionRequest {

        String Type;
        String requestedBy;
        String Status;

        public DTOPermissionRequest(String type, String requestedBy, String status) {
            this.Type = type;
            this.requestedBy = requestedBy;
            this.Status = status;
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

}
