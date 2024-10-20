package servlets;

import JsonSerializer.JsonSerializer;
import allsheetsmanager.AllSheetsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.Map;


@WebServlet(name = "filter sheet Servlet", urlPatterns = {"/filter-sheet"})
public class FilterSheetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get parameters from the request
        String sheetName = req.getParameter("sheetName");
        String from = req.getParameter("from");
        String to = req.getParameter("to");
        String selectedValuesJson = req.getParameter("selectedValues");


        // Check if all required parameters are present
        if (sheetName == null || from == null || to == null || selectedValuesJson == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing required parameters: sheetName, from, to, or selectedValues.");
            return;
        }


        try {
            // Convert the selectedValuesJson into a Map<String, List<String>> using Gson
            Gson gson = new Gson();
            Map<String, List<String>> selectedColumnValues = gson.fromJson(selectedValuesJson, new TypeToken<Map<String, List<String>>>(){}.getType());

            // Get AllSheetsManager
            AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            // Get SheetManager by sheet name
            SheetManager sheetManager = sheetsManager.getSheet(sheetName);
            // Get the sorted sheet
            DTOSheet dtoSheet = sheetManager.filterSheet(selectedColumnValues,from, to);
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
            resp.getWriter().write("Error processing filter  request: " + e.getMessage());
        }

    }

}