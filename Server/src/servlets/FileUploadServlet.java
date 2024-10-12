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

import java.io.IOException;
import java.io.InputStream;

@WebServlet(name= "upload file",urlPatterns = "/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());

        // Retrieve the uploaded file from the form-data with the name 'file'
        Part filePart = request.getPart("file"); // "file" is the key name for the file in the form-data
        if (filePart != null) {
            // Get the file name
            String fileName = filePart.getSubmittedFileName();

            // Get the file content as InputStream
            InputStream fileContent = filePart.getInputStream();

            try{
                sheetsManager.addSheet(fileContent,fileName);
            }catch(Exception e){
                response.getWriter().println(e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
        else
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
