package servlets;

import JsonSerializer.JsonSerializer;
import dto.DTOSheetInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import allsheetsmanager.AllSheetsManager;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

//@WebServlet(name= "upload file",urlPatterns = "/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String userName = SessionUtils.getUsername(request);
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();

        String fileName = null; // To store the file name
        InputStream inputStream = null;

        for (Part part : parts) {
            if (part.getName().equals("file")) { // Look for the file part
                // Log part details and read the file
                inputStream = part.getInputStream();

                //todo : maybe delete
                fileName = part.getSubmittedFileName();

            }
        }
        // Pass the InputStream and file name to the addSheet method
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        try {

            String fileTitle = sheetsManager.addSheet(inputStream, fileName,userName); // Call the addSheet method



            int numOfRows = sheetsManager.getAllSheetsManager().get(fileTitle).getNumRows();
            int numOfCols = sheetsManager.getAllSheetsManager().get(fileTitle).getNumCols();

            DTOSheetInfo dtoSheetInfo = new DTOSheetInfo(fileTitle,numOfRows,numOfCols,userName);

            JsonSerializer jsonSerializer = new JsonSerializer();
            String jsonString = jsonSerializer.convertDTOSheetInfoToJson(dtoSheetInfo);

            // Send the sheet info JSON back to the client
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(jsonString);
            out.flush();


        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error processing file: " + e.getMessage());
            return; // Exit if there's an error
        }

    }

}
