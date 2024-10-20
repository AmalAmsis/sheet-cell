package servlets;

import JsonSerializer.JsonSerializer;
import allsheetsmanager.AllSheetsManager;
import dto.DTOSheet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import utils.ServletUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "sort sheet Servlet", urlPatterns = {"/sort-sheet"})
public class SortSheetServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get parameters from the request
        String sheetName = req.getParameter("sheetName");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String columnsPrioritiesStr =req.getParameter("prioritiesList");

        // Check if all required parameters are present
        if (sheetName == null || from == null || to == null || columnsPrioritiesStr == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing required parameters: username or sheetName.");
            return;
        }

        try{
            // Convert columnsPrioritiesStr to a List<Character>
            List<Character> columnsPriorities = columnsPrioritiesStr.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.toList());

            // Get AllSheetsManager
            AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            // Get SheetManager by sheet name
            SheetManager sheetManager = sheetsManager.getSheet(sheetName);
            // Get the sorted sheet
            DTOSheet dtoSheet = sheetManager.getSortedSheet(from,to,columnsPriorities);
            // Convert the sheet to JSON
            JsonSerializer jsonSerializer = new JsonSerializer();
            String jsonString = jsonSerializer.convertDtoToJson(dtoSheet);

            // Return the JSON to the client
            resp.setContentType("application/json");
            resp.getWriter().write(jsonString);
            resp.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            // Handle general errors
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Error processing sort request: " + e.getMessage());
        }

    }
}
