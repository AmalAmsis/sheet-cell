package servlets;

import JsonSerializer.GsonUtil;
import allsheetsmanager.AllSheetsManager;
import com.google.gson.Gson;
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

@WebServlet(name = "Version Changes Servlet", urlPatterns = {"/version-changes"})
public class VersionChangesServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String sheetName =SessionUtils.getSheetTitle(req);
            AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            SheetManager sheetManager = sheetsManager.getSheet(sheetName);
            SheetStateManager sheetStateManager = sheetManager.getCurrentSheetState();
            List<SheetVersionData> versionDataList = sheetStateManager.getVersionHandler().getVersionHistory();

            List<Integer> changesPerVersion = new ArrayList<>();

            for (SheetVersionData versionData : versionDataList){
                int numOfChanges = versionData.getNumOfUpdateCells();
                changesPerVersion.add(numOfChanges);
            }

            Gson gson = new Gson();
            String jsonString = gson.toJson(changesPerVersion);

            // Return the JSON to the client
            resp.setContentType("application/json");
            resp.getWriter().write(jsonString);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
        // Handle general errors
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        resp.getWriter().write( e.getMessage());
         }
    }


}
