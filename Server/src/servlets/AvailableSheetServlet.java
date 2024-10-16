package servlets;


import allsheetsmanager.AllSheetsManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name= "available sheets",urlPatterns = "/available-sheets")
public class AvailableSheetServlet extends HttpServlet {


    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        // Get the number of sheets the client already has (sent as a query parameter)
        String clientSheetCountParam = request.getParameter("clientSheetCount");
        int clientSheetCount = clientSheetCountParam != null ? Integer.parseInt(clientSheetCountParam) : 0;


        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());



        try {
            // Retrieve the list of available sheets (this is just an example, adjust according to your class)
            List<String> availableSheets = sheetsManager.getAllSheets(); // Assume getAllSheets() returns a list of available sheet names


            if (availableSheets == null || availableSheets.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else if (availableSheets.size() > clientSheetCount) {
                // If the server has more sheets than the client, send the full list
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                String jsonResponse = buildJsonResponse(availableSheets);
                // Return the JSON response
                out.print(jsonResponse);
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
