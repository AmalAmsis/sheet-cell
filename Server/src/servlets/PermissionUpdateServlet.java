package servlets;

import dto.DTOPermissionRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import permission.permissionManager.PermissionManager;
import permission.sheetPermission.PermissionRequest;
import permission.sheetPermission.SheetPermission;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import JsonSerializer.JsonSerializer;

@WebServlet(name= "permission update",urlPatterns = "/permission-update")
public class PermissionUpdateServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sheetName = request.getParameter("sheetName");
        if (sheetName == null || sheetName.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing sheetName parameter.");
            return;
        }

        String json = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

        JsonSerializer jsonSerializer = new JsonSerializer();
        Map<String, DTOPermissionRequest> permissionsMap = jsonSerializer.convertJsonToPermissionsMap(json);

        if (permissionsMap == null || permissionsMap.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid or empty permissions data.");
            return;
        }

        PermissionManager permissionManager = ServletUtils.getPermissionManager(getServletContext());
        SheetPermission sheetPermission = permissionManager.getSheetPermissions(sheetName);

        if (sheetPermission == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sheet " + sheetName + " not found.");
            return;
        }


        for (Map.Entry<String, DTOPermissionRequest> entry : permissionsMap.entrySet()) {
            String userName = entry.getKey();
            DTOPermissionRequest dtoRequest = entry.getValue();
            String permissionType = dtoRequest.getType();
            String status = dtoRequest.getStatus();

            PermissionRequest currentPermission = sheetPermission.getSheetPermissions().get(userName);


            if (currentPermission != null) {
                if (permissionType.equals(currentPermission.getType())) {
                    sheetPermission.getSheetPermissions().put(userName, new PermissionRequest(permissionType, userName, status));
                }
                if (permissionType.equals(currentPermission.getNewRequestType())) {
                    if ("APPROVED".equals(status)) {
                        sheetPermission.getSheetPermissions().put(userName, new PermissionRequest(permissionType, userName, status));

                    }
                    if ("DENIED".equals(status)) {
                        currentPermission.setNewRequestStatus(status);
                    }
                }
            }
        }


        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Permissions updated successfully.");
    }


}


