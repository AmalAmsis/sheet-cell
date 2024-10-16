package servlets;

import allsheetsmanager.AllSheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.Properties;

public class CellServlet extends HttpServlet {

    //מידע על תא קיים
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {


    }

    //עדכון תא
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        Properties prop = new Properties();
        prop.load(req.getInputStream());

        String sheetName = prop.getProperty("sheetName");
        String coordinateRaw = prop.getProperty("coordinate");
        String originalValueRaw = prop.getProperty("originalValue");

        // בדיקה אם יש ערכים חסרים
        if (sheetName == null || coordinateRaw == null || originalValueRaw == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getOutputStream().println("{\"error\": \"Missing parameters\"}");
            return;
        }

        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);
        try {
            sheetManager.updateCell(coordinateRaw, originalValueRaw);
            // שליחת תשובת JSON במקרה של הצלחה
            resp.getWriter().write("{\"status\": \"success\"}");
        } catch (Exception e) {
            // טיפול בשגיאה בעת עדכון תא
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getOutputStream().println("{\"error\": \"update failed: " + e.getMessage() + "\"}");
        }
    }



}
