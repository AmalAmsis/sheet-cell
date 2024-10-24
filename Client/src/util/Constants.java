package util;

import com.google.gson.Gson;

public class Constants {

    public final static String NO_USERNAME = " ";


    // global constants
    public final static int REFRESH_RATE = 2000;

    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/component/login/login.fxml";
    public final static String SELECTED_SHEET_VIEW_FXML_RESOURCE_LOCATION = "/component/selectedSheetView/main/selectedSheetView.fxml";
    public final static String DASHBOARD_FXML_RESOURCE_LOCATION = "/component/dashboard/main/maindashboard/dashboard.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/component/main/sheetCellAppMain.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String LOAD = FULL_SERVER_PATH + "/upload-file";
    public final static String VIEW = FULL_SERVER_PATH + "/sheet";
    public final static String AVAILABLE_SHEETS = FULL_SERVER_PATH + "/available-sheets";
    public final static String SORT_SHEET = FULL_SERVER_PATH + "/sort-sheet";
    public final static String FILTER_SHEET = FULL_SERVER_PATH + "/filter-sheet";
    public final static String UPDATE_CELL = FULL_SERVER_PATH + "/cell";
    public final static String CURRENT_SHEET_NAME = FULL_SERVER_PATH + "/current-sheet-name";
    public final static String CURRENT_SHEET_VERSION = FULL_SERVER_PATH + "/sheet/checking-for-an-update";
    public final static String VERSION_CHANGES = FULL_SERVER_PATH + "/version-changes";
    public final static String VERSION_SHEET = FULL_SERVER_PATH + "/version-sheet";
    public final static String RANGE = FULL_SERVER_PATH + "/range";
    public final static String RANGES = FULL_SERVER_PATH + "/ranges";
    public final static String DYNAMIC_ANALYSIS = FULL_SERVER_PATH + "/dynamic-analysis";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();



}
