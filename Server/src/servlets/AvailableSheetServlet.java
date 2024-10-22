package servlets;


import JsonSerializer.JsonSerializer;
import allsheetsmanager.AllSheetsManager;
import dto.DTOSheetInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sheetmanager.SheetManager;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name= "available sheets",urlPatterns = "/available-sheets")
public class AvailableSheetServlet extends HttpServlet {


    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Get the number of sheets the client already has (sent as a query parameter)
        String clientSheetCountParam = request.getParameter("clientSheetCount");
        int clientSheetCount = clientSheetCountParam != null ? Integer.parseInt(clientSheetCountParam) : 0;
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());

        try {
           Map<String,SheetManager> availableSheets = sheetsManager.getAllSheetsManager();

            if (availableSheets == null || availableSheets.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else if (availableSheets.size() > clientSheetCount) {
                // If the server has more sheets than the client, send the full list
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                // Create a list to store sheet information
                List<DTOSheetInfo> sheetInfoList = new ArrayList<>();

                for (Map.Entry<String, SheetManager> entry : availableSheets.entrySet()) {

                    String sheetName = entry.getKey();
                    SheetManager sheetManager = entry.getValue();
                    int numRows =sheetManager.getNumRows();
                    int numCols =sheetManager.getNumCols();
                    String uploadBy =sheetManager.getOwner();

                    sheetInfoList.add(new DTOSheetInfo(sheetName, numRows, numCols,uploadBy));
                }


                JsonSerializer jsonSerializer = new JsonSerializer();
                String jsonString = jsonSerializer.convertDTOSheetInfoListToJson(sheetInfoList);
                // Return the JSON response
                out.print(jsonString);
                out.flush();
                out.close(); // Ensure the PrintWriter is closed properly

            }else{
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error retrieving available sheets: " + e.getMessage());
        }
    }


    // Helper method to convert list to JSON string
    private String buildJsonResponse(List<String> availableSheets) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < availableSheets.size(); i++) {
            json.append("\"").append(availableSheets.get(i)).append("\"");
            if (i < availableSheets.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }




}
