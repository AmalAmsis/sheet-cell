package servlets;

import JsonSerializer.JsonSerializer;
import allsheetsmanager.AllSheetsManager;
import constants.Constants;
import dto.DTOSheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import permission.permissionManager.PermissionManager;
import permission.sheetPermission.SheetPermission;
import sheetmanager.SheetManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Objects;


@WebServlet(name = "sheetServlet", urlPatterns = {"/sheet"})
public class SheetServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String sheetName = req.getParameter("sheetName");
        String username = SessionUtils.getUsername(req);

        if (sheetName == null || username == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing required parameters: username or sheetName.");
            return;
        }

        // Check permissions for the requested sheet
        PermissionManager permissionManager = ServletUtils.getPermissionManager(getServletContext());
        SheetPermission sheetPermission = permissionManager.getSheetPermissions(sheetName);

        // Handle case where the sheet does not exist in permissions manager
        if(sheetPermission == null)
        {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Sheet not found or permissions not available.");
            return;
        }

        // Check if the user is the owner or has permissions
        if(!username.equals(sheetPermission.getOwner())) {

            if(sheetPermission.getSheetPermissions()==null)
            {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Access denied: You do not have permission to view this sheet.");
                return;
            }

            // If user has no permission
            if(sheetPermission.getUserPermission(username) == null) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Access denied: You do not have permission to view this sheet.");
                return;
            }

            String userPermission = sheetPermission.getUserPermission(username).getStatus();
            // If user permission request is pending approval
            if("PENDING".equals(userPermission)){
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter().write("Access denied: Your request for permission for this sheet is still awaiting approval from the owner.");
                return;
            }

        }

        // Load the requested sheet and send it as a JSON response
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);
        if (sheetManager == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Sheet data not found.");
            return;
        }

        // Convert sheet data to JSON
        DTOSheet dtoSheet = sheetManager.displaySheet();
        JsonSerializer jsonSerializer = new JsonSerializer();
        String jsonString = jsonSerializer.convertDtoToJson(dtoSheet);

        // Set response content type and send JSON response
        resp.setContentType("application/json");
        resp.getWriter().write(jsonString);

        // Store sheet details in the session
        req.getSession(true).setAttribute(Constants.FILENAME, sheetName);
        req.getSession(true).setAttribute(Constants.SHEET_VERSION, dtoSheet.getSheetVersion());
    }
}
