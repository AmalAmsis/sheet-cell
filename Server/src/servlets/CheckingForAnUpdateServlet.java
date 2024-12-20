package servlets;

import allsheetsmanager.AllSheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "CheckingForAnUpdateServlet", urlPatterns = {"/sheet/checking-for-an-update"})
public class CheckingForAnUpdateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Retrieve the sheet name from the session
        String sheetName = SessionUtils.getSheetTitle(req);
        int currentVersion = SessionUtils.getSheetVersion(req);

        if (sheetName == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().println("Missing sheet title");
            return;
        }


        // Get the current version of the sheet (this needs to be tracked)
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);  // Track versions
        int latestVersion = sheetManager.getSheetVersion();

        if (latestVersion > currentVersion) {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }
}

