package utils;

import jakarta.servlet.ServletContext;
import allsheetsmanager.AllSheetsManager;


import jakarta.servlet.http.HttpServletRequest;
import permission.permissionManager.PermissionManager;
import permission.permissionManager.PermissionManagerImpl;
import users.UserManager;
import users.UserManagerImpl;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String SHEETS_MANAGER_ATTRIBUTE_NAME = "allsheetsmanager";

    private static final Object sheetManagerLock = new Object();

    private static final String PERMISSION_MANAGER_ATTRIBUTE_NAME = "permissionManager";
    private static final Object permissionManagerLock = new Object();


    public static AllSheetsManager getSheetManager(ServletContext servletContext){
        synchronized (sheetManagerLock) {
            if (servletContext.getAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME, new AllSheetsManager());
            }
        }
        return (AllSheetsManager) servletContext.getAttribute(SHEETS_MANAGER_ATTRIBUTE_NAME);

    }


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


    //todo: permission
    public static PermissionManager getPermissionManager(ServletContext servletContext) {
        synchronized (permissionManagerLock) {
            if (servletContext.getAttribute(PERMISSION_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(PERMISSION_MANAGER_ATTRIBUTE_NAME, new PermissionManagerImpl());
            }
        }
        return (PermissionManager) servletContext.getAttribute(PERMISSION_MANAGER_ATTRIBUTE_NAME);
    }

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
