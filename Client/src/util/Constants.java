package util;

import com.google.gson.Gson;

public class Constants {


    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/component/login/login.fxml";

    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    public final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/Server_Web_exploded";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginShortResponse";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logout";
    public final static String REGISTER = FULL_SERVER_PATH +  "/upload-file";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();



}
