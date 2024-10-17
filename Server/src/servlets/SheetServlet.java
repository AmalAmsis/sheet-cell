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

import java.io.IOException;


@WebServlet(name = "sheetServlet", urlPatterns = {"/sheet"})
public class SheetServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String username = req.getParameter("username");// אמורים לדעת את ה username לפי ה session
        String sheetName = req.getParameter("sheetName");


        //todo - add permission
//        if(permissionUsersManager.getPermission(username,sheetName) == NO_PERMISSION){
//
//        }

        if (username == null || sheetName == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Missing required parameters: username or sheetName.");
            return;

        } else {

            //set the username in a session so it will be available on each request
            //the true parameter means that if a session object does not exists yet
            //create a new one
            req.getSession(true).setAttribute(Constants.FILENAME, sheetName);

            AllSheetsManager sheetsManager = ServletUtils.getSheetManager(getServletContext());
            SheetManager sheetManager = sheetsManager.getSheet(sheetName);
            DTOSheet dtoSheet = sheetManager.displaySheet();
            JsonSerializer jsonSerializer = new JsonSerializer();

            String jsonString = jsonSerializer.convertDtoToJson(dtoSheet);

            resp.setContentType("application/json");
            resp.getWriter().write(jsonString);

        }
    }
}
