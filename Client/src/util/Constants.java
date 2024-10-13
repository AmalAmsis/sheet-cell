package util;

import com.google.gson.Gson;

public class Constants {

    public final static String NO_USERNAME = " ";


    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/component/login/login.fxml";
    public final static String MAIN_PAGE_FXML_RESOURCE_LOCATION = "/component/main/sheetCellAppMain.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();



}
