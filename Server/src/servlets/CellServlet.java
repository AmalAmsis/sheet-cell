package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import allsheetsmanager.AllSheetsManager;
import sheetmanager.SheetManager;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet(name = "Cell", urlPatterns = {"/cell"})
public class CellServlet extends HttpServlet {

    // Update cell
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Read the request body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Parse the JSON request body
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(sb.toString()).getAsJsonObject();
        String coordinateRaw = json.get("coordinate").getAsString();
        String originalValueRaw = json.get("originalValue").getAsString();

        String sheetName = SessionUtils.getSheetTitle(req);
        String userName = SessionUtils.getUsername(req);

        if (userName == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized user");
            return;
        }

        if (sheetName == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Unauthorized sheet");
            return;
        }

        if (coordinateRaw == null || originalValueRaw == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Missing parameters");
            return;
        }

        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        try {
            synchronized (getServletContext()) {
                sheetManager.updateCell(coordinateRaw, originalValueRaw, userName);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().println(sheetName);
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().println("Update failed: " + e.getMessage());
        }
    }
}
