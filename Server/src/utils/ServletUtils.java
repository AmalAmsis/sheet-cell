package utils;

import engine.Engine;
import engine.EngineImpl;
import jakarta.servlet.ServletContext;
import sheetsManager.SheetsManager;


import jakarta.servlet.http.HttpServletRequest;
import users.UserManager;
import users.UserManagerImpl;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String CHAT_MANAGER_ATTRIBUTE_NAME = "chatManager";
    private static final String SHEETS_MANAGER_ATTRIBUTE_NAME = "sheetsManager";

    private static final Object sheetManagerLock = new Object();


    public static SheetsManager getSheetManager(ServletContext servletContext){
        synchronized (sheetManagerLock) {
            if (servletContext.getAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME, new SheetsManager());
            }
        }
        return (SheetsManager) servletContext.getAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME);

    }

    //private static final String SHEET_CELL_MANAGER_ATTRIBUTE_NAME = "sheetCellManager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object userManagerLock = new Object();
    //private static final Object sheetCellManagerLock = new Object();

    public static UserManager getUserManager(ServletContext servletContext) {

        synchronized (userManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManagerImpl());
            }
        }
        return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

//    public static Engine getChatManager(ServletContext servletContext) {
//        synchronized (sheetCellManagerLock) {
//            if (servletContext.getAttribute(SHEET_CELL_MANAGER_ATTRIBUTE_NAME) == null) {
//                servletContext.setAttribute(SHEET_CELL_MANAGER_ATTRIBUTE_NAME, new EngineImpl() {
//                });
//            }
//        }
//        return (Engine) servletContext.getAttribute(SHEET_CELL_MANAGER_ATTRIBUTE_NAME);
//    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
