package servlets;

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
import java.io.PrintWriter;

@WebServlet(name = "user permission Servlet", urlPatterns = {"/user-permission"})

public class UserPermissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String sheetName = request.getParameter("sheetName");
        String username = SessionUtils.getUsername(request);

        if (sheetName == null || username == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Missing required parameters: username or sheetName.");
            return;
        }

        // Retrieve the PermissionManager
        PermissionManager permissionManager = ServletUtils.getPermissionManager(getServletContext());
        SheetPermission sheetPermission = permissionManager.getSheetPermissions(sheetName);

        if (sheetPermission == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Sheet not found.");
            return;
        }

        String userPermission = "NONE";

        // Check if the user is the owner
        if (username.equals(sheetPermission.getOwner())) {
            userPermission = "OWNER";
        } else {
            PermissionRequest permissionRequest = sheetPermission.getUserPermission(username);

            if (permissionRequest != null && !"PENDING".equals(userPermission)) {
                userPermission = permissionRequest.getType();
            }
        }

        // Send the user's permission level as the response
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.write(userPermission);
        out.flush();
    }
}
