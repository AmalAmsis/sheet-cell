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
import sheetmanager.SheetManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

@WebServlet(name = "Dynamic Analysis Servlet", urlPatterns = "/dynamic-analysis")
public class DynamicAnalysisServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // קבלת פרמטרים
        String coordinate = request.getParameter("coordinate");
        String value = request.getParameter("value");
        String sheetName = SessionUtils.getSheetTitle(request);

        // בדיקת פרמטרים חסרים
        if (coordinate == null || value == null || sheetName == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        // בדיקת האם value מייצג מספר (double)
        try {
            Double.parseDouble(value); // אם זה לא מספר, ייזרק חריג
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid value: must be a number");
            return;
        }

        // שליפת ה-SheetManager והעדכון
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        // ביצוע העדכון הזמני
        synchronized (getServletContext()) {
            try {
                DTOSheet dtoSheet = sheetManager.updateTemporaryCellValue(coordinate, value);


                // המרת ה-DTO לפורמט JSON ושליחת התשובה
                JsonSerializer jsonSerializer = new JsonSerializer();
                String jsonString = jsonSerializer.convertDtoToJson(dtoSheet);

                response.setContentType("application/json");
                response.getWriter().write(jsonString);
            }catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }
}
