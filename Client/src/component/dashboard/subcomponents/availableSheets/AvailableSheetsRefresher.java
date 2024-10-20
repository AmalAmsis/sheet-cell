package component.dashboard.subcomponents.availableSheets;

import JsonSerializer.JsonSerializer;
import dto.DTOSheetInfo;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import util.http.HttpClientUtil;

import static util.Constants.AVAILABLE_SHEETS;

public class AvailableSheetsRefresher extends TimerTask {

    private AvailableSheetsController availableSheetsController;
    private int lastKnownSheetCount = 0; // Track the number of sheets the client knows about

    public AvailableSheetsRefresher(AvailableSheetsController controller) {
        this.availableSheetsController = controller;
    }

    @Override
    public void run() {
        String url = AVAILABLE_SHEETS + "?clientSheetCount=" + lastKnownSheetCount; // Send the current count to the server

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Call call = HttpClientUtil.HTTP_CLIENT.newCall(request);

        try {
            Response response = call.execute();

            if (response.isSuccessful()) {
                String jsonResponse = response.body().string();

                JsonSerializer jsonSerializer = new JsonSerializer();
                List<DTOSheetInfo> dtoSheetInfoList = jsonSerializer.convertJsonToDtoInfoList(jsonResponse);

                if (dtoSheetInfoList != null && dtoSheetInfoList.size() > lastKnownSheetCount) {
                    lastKnownSheetCount = dtoSheetInfoList.size(); // Update the last known count


                    availableSheetsController.refreshAvailableSheets(dtoSheetInfoList);

                } else if (dtoSheetInfoList == null) {
                    System.out.println("Parsed newSheets is null");
                }
            } else {
                System.out.println("Failed to fetch sheets. Response code: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Error in refresher: " + e.getMessage());
        }
    }
}
