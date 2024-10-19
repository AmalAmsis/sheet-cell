package utils;

import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getSheetTitle (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.FILENAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static int getSheetVersion (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.SHEET_VERSION) : null;
        return sessionAttribute != null ? Integer.parseInt(sessionAttribute.toString()) : null;

    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
