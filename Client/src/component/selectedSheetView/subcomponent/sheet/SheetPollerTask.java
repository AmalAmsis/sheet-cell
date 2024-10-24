package component.selectedSheetView.subcomponent.sheet;

import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import util.http.HttpClientUtil;

import java.io.IOException;
import java.util.TimerTask;

import static util.Constants.CURRENT_SHEET_VERSION;

public class SheetPollerTask extends TimerTask {

    private final Button switchToTheLatestVersionButton;
    private boolean isFlashing = false;  // כדי לעקוב האם הכפתור כבר מהבהב
    private Timeline flashTimeline;  // נשתמש ב-Timeline כדי להפעיל את ההבהוב

    public SheetPollerTask(Button switchToTheLatestVersionButton) {
        this.switchToTheLatestVersionButton = switchToTheLatestVersionButton;
    }

    @Override
    public void run() {
        String versionCheckUrl = HttpUrl.parse(CURRENT_SHEET_VERSION).newBuilder().build().toString();

        Request request = new Request.Builder().url(versionCheckUrl).build();

        HttpClientUtil.HTTP_CLIENT.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.err.println("Failed to poll for updates: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //currentVersion = latestVersion;  // נעדכן לגרסה האחרונה
                    if (!isFlashing) {  // נתחיל הבהוב רק אם הוא לא התחיל כבר
                        Platform.runLater(() -> startButtonFlashing());
                        isFlashing = true;  // עדכון שהכפתור מהבהב
                    }
                } else {
                    // אם הגרסה כבר עדכנית, נוודא שאין הבהוב
                    if (isFlashing) {
                        Platform.runLater(() -> stopButtonFlashing());
                        isFlashing = false;  // עדכון שההבהוב הופסק
                    }
                }
                response.body().close();
                //return null;
            }
        });
    }



    // נתחיל הבהוב רק אם יש גרסה חדשה
    private void startButtonFlashing() {
        flashTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> switchToTheLatestVersionButton.setStyle("-fx-background-color: red;")),
                new KeyFrame(Duration.seconds(1), e -> switchToTheLatestVersionButton.setStyle(""))
        );
        flashTimeline.setCycleCount(Timeline.INDEFINITE);  // הכפתור יבהב בלי לעצור
        flashTimeline.play();
    }

    // נפסיק את ההבהוב כשאין צורך
    private void stopButtonFlashing() {
        if (flashTimeline != null) {
            flashTimeline.stop();  // נוודא שההבהוב עוצר
        }
        switchToTheLatestVersionButton.setStyle("");  // נחזיר את הסגנון הרגיל לכפתור
    }
}
