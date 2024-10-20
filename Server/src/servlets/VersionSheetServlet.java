package servlets;

import JsonSerializer.JsonSerializer;
import allsheetsmanager.AllSheetsManager;
import com.google.gson.Gson;
import dto.DTOSheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import sheetmanager.sheet.version.SheetVersionData;
import sheetmanager.state.SheetStateManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Version Sheet Servlet", urlPatterns = {"/version-sheet"})
public class VersionSheetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String versionStr = req.getParameter("version");

        try {
            int version = Integer.parseInt(versionStr);  // Convert the version string to an integer

            String sheetName = SessionUtils.getSheetTitle(req);
            AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            SheetManager sheetManager = sheetsManager.getSheet(sheetName);
            SheetStateManager sheetStateManager = sheetManager.getCurrentSheetState();

            DTOSheet dtoSheet = sheetStateManager.getVersionHandler().getSheetByVersion(version);
            JsonSerializer jsonSerializer = new JsonSerializer();

            String jsonString = jsonSerializer.convertDtoToJson(dtoSheet);

            // Return the JSON to the client
            resp.setContentType("application/json");
            resp.getWriter().write(jsonString);
            resp.setStatus(HttpServletResponse.SC_OK);


        } catch (NumberFormatException e) {
            // Handle the case where the version parameter is not a valid integer
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Invalid version parameter: " + versionStr);

        }
    }
}