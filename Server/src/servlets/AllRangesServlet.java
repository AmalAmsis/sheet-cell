package servlets;

import allsheetsmanager.AllSheetsManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

@WebServlet(name = "all ranges servlet", urlPatterns = {"/ranges"})
public class AllRangesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve the sheet name from session
        String sheetName = SessionUtils.getSheetTitle(request);
        if (sheetName == null) {
            // If the sheet name is not found, respond with Unauthorized status
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Unauthorized sheet");
            return;
        }

        // Retrieve the SheetManager for the current sheet
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        try {
            // Synchronize on the servlet context to ensure thread safety
            synchronized (getServletContext()) {
                // Get all the ranges in the sheet
                List<DTORange> ranges = sheetManager.getAllRanges();
                // Convert the DTO ranges to a list of range names
                List<String> rangesNames = TurnDtoRangeListIntoRangesNameList(ranges);

                // Convert the list of range names to JSON
                Gson gson = new Gson();
                String jsonResponse = gson.toJson(rangesNames);

                // Set the response type to JSON and return the JSON response
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(jsonResponse);
                out.flush();
            }
        } catch (Exception e) {
            // Handle any errors and respond with an internal server error status
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
        }
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Use Gson to parse the request body (expected to be JSON)
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);

        // Extract the necessary fields from the JSON object
        String fromCoordinate = json.get("fromCoordinate").getAsString();
        String toCoordinate = json.get("toCoordinate").getAsString();
        String newRangeName = json.get("newRangeName").getAsString();

        // Validate that all required fields are present and not empty
        if (newRangeName == null || newRangeName.isEmpty() || fromCoordinate == null || fromCoordinate.isEmpty() || toCoordinate == null || toCoordinate.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing parameters");
            return;
        }

        // Retrieve the sheet name from session
        String sheetName = SessionUtils.getSheetTitle(request);
        if (sheetName == null) {
            // If the sheet name is not found, respond with Unauthorized status
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Unauthorized sheet");
            return;
        }

        // Retrieve the SheetManager for the current sheet
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        try {
            // Synchronize on the servlet context to ensure thread safety
            synchronized (getServletContext()) {
                // Add the new range to the sheet
                sheetManager.addNewRange(newRangeName, fromCoordinate, toCoordinate);
                // Respond with OK status after successful range addition
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            // Handle any errors and respond with an internal server error status
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
        }
    }


    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Use Gson to parse the request body (expected to be JSON)
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);

        // Extract the range name to delete from the JSON object
        String rangeName = json.get("RangeName").getAsString();

        // Validate that the range name is present and not empty
        if (rangeName == null || rangeName.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Missing parameters");
            return;
        }

        // Retrieve the sheet name from session
        String sheetName = SessionUtils.getSheetTitle(request);
        if (sheetName == null) {
            // If the sheet name is not found, respond with Unauthorized status
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("Unauthorized sheet");
            return;
        }

        // Retrieve the SheetManager for the current sheet
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        SheetManager sheetManager = sheetsManager.getSheet(sheetName);

        try {
            // Synchronize on the servlet context to ensure thread safety
            synchronized (getServletContext()) {
                // Remove the specified range from the sheet
                sheetManager.removeRange(rangeName);
                // Respond with OK status after successful range deletion
                response.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (Exception e) {
            // Handle any errors and respond with an internal server error status
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
        }
    }

    // Helper method to convert a list of DTORange objects to a list of range names
    private List<String> TurnDtoRangeListIntoRangesNameList(List<DTORange> ranges) {
        List<String> rangesNameList = new ArrayList<>();
        for (DTORange range : ranges) {
            rangesNameList.add(range.getName());
        }
        return rangesNameList;
    }

}
