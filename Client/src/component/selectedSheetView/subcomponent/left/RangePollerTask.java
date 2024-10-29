package component.selectedSheetView.subcomponent.left;

import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;

import static util.Constants.*;

public class RangePollerTask extends TimerTask {

    private List<String> currentRanges;
    private final SelectedSheetViewLeftController selectedSheetViewLeftController;

    public RangePollerTask(SelectedSheetViewLeftController selectedSheetViewLeftController, List<String> initialRanges) {
        this.selectedSheetViewLeftController = selectedSheetViewLeftController;
        this.currentRanges = initialRanges;
    }

    @Override
    public void run() {
        // יצירת URL לבקשה
        String url = HttpUrl.parse(RANGES).newBuilder().build().toString();
        Request request = new Request.Builder().url(url).build();

        try (Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonResponse = response.body().string();
                List<String> newRanges = GSON_INSTANCE.fromJson(jsonResponse, new TypeToken<List<String>>(){}.getType());

                // עדכון ה-checkbox במקרה של רשימת ריינג'ים שונה
                if (!newRanges.equals(currentRanges)) {
                    Platform.runLater(() -> {
                        selectedSheetViewLeftController.updateChoiceBoxesWithNewRanges(newRanges);
                    });
                    currentRanges = newRanges;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to poll ranges: " + e.getMessage());
        }
    }
}

