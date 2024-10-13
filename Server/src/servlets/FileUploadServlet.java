package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import allsheetsmanager.AllSheetsManager;
import utils.ServletUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Scanner;

@WebServlet(name= "upload file",urlPatterns = "/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        Collection<Part> parts = request.getParts();

        String fileName = null; // To store the file name
        InputStream inputStream = null;

        for (Part part : parts) {
            if (part.getName().equals("file")) { // Look for the file part
                // Log part details and read the file
                inputStream = part.getInputStream();
                fileName = part.getSubmittedFileName();

            }
        }
        // Pass the InputStream and file name to the addSheet method
        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
        try {
            sheetsManager.addSheet(inputStream, fileName); // Call the addSheet method
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println("Error processing file: " + e.getMessage());
            return; // Exit if there's an error
        }

    }


    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("bla bla bla");
    }

}
