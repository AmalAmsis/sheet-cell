package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import permission.permissionManager.PermissionManager;
import permission.sheetPermission.PermissionRequest;
import permission.sheetPermission.SheetPermission;
import users.UserManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name= "sheet permission",urlPatterns = "/sheet-permission")
public class SheetPermissionServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sheetName = request.getParameter("sheetName");

        PermissionManager permissionManager = ServletUtils.getPermissionManager(getServletContext());
        SheetPermission sheetPermission = permissionManager.getSheetPermissions(sheetName);

        if (sheetPermission == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }

        Map<String, PermissionRequest> permissionsMap = sheetPermission.getSheetPermissions();
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(permissionsMap);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

    }


    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sheetName = request.getParameter("sheetName");
        String type = request.getParameter("type");
        String status = request.getParameter("status");
        if (sheetName == null || type == null || status == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters: sheetName, type, status, or username");
            return;
        }

        PermissionManager permissionManager = ServletUtils.getPermissionManager(getServletContext());
        String username = SessionUtils.getUsername(request);

        SheetPermission sheetPermission = permissionManager.getSheetPermissions(sheetName);
        if (sheetPermission == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }


        // Check if the user is the OWNER
        if ("OWNER".equals(sheetPermission.getOwner())) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            response.getWriter().write("As the OWNER, you already have full permissions on this sheet.");
            return;
        }


        // Check if the user already has a request for the same permission type
        PermissionRequest existingRequest = sheetPermission.getSheetPermissions().get(username);
        if (existingRequest != null) {
            if (existingRequest.getType().equals(type)) {
                if ("PENDING".equals(existingRequest.getStatus())) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("A request for this permission is already pending approval.");
                    return;
                } else if ("APPROVED".equals(existingRequest.getStatus())) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    response.getWriter().write("You already have this permission: " + type);
                    return;
                }
            }
        }

        // If no existing permission or no conflict, add new request
        PermissionRequest permissionRequest = new PermissionRequest(type,username,status);
        sheetPermission.getSheetPermissions().put(username,permissionRequest);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Permission request added successfully.");

    }

}
