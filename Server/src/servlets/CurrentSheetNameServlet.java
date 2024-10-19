package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Current Sheet Name Servlet", urlPatterns = "/current-sheet-name")
public class CurrentSheetNameServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String sheetName = SessionUtils.getSheetTitle(request);
        if (sheetName == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.println("Unauthorized sheet");
            return;
        }
        else {
            response.setStatus(HttpServletResponse.SC_OK);
            out.println(sheetName);
            return;
        }

    }
}
