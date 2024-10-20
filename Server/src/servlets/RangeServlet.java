package servlets;

import allsheetsmanager.AllSheetsManager;
import com.google.gson.Gson;
import dto.DTOCoordinate;
import dto.DTORange;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Range", urlPatterns = {"/range"})
public class RangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // שליפת שם ה-range וה-sheet
        String rangeName = request.getParameter("rangeName");
        String sheetName = SessionUtils.getSheetTitle(request);

        if (rangeName == null || sheetName == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing parameters");
            return;
        }

        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        try {
            synchronized (getServletContext()) {
                // שליפת ה-range לפי שם
                DTORange range = sheetManager.getRange(rangeName);
                List<DTOCoordinate> coordinates = range.getCoordinates();
                List<String> coordinatesString = TurnDtoCoordinateListToCellIdList(coordinates);

                // המרת הרשימה ל-JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(coordinates);

                // הגדרת סוג התוכן כ-JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // שליחת התשובה
                PrintWriter out = response.getWriter();
                out.print(jsonResponse);
                out.flush();
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // Helper method to convert DTOCoordinate list to cell ID list
    private List<String> TurnDtoCoordinateListToCellIdList(List<DTOCoordinate> dtoCoordinateList) {
        List<String> cellsId = new ArrayList<>();
        for (DTOCoordinate dtoCoordinate : dtoCoordinateList) {
            cellsId.add(dtoCoordinate.toString());
        }
        return cellsId;
    }
}
