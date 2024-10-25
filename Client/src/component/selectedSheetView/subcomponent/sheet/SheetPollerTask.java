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
    private boolean isFlashing = false;
    private Timeline flashTimeline;

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
                try {
                    if (response.isSuccessful()) {
                        if (!isFlashing) {
                            Platform.runLater(SheetPollerTask.this::startButtonFlashing);
                            isFlashing = true;
                        }
                    } else if (isFlashing) {
                        Platform.runLater(SheetPollerTask.this::stopButtonFlashing);
                        isFlashing = false;
                    }
                } finally {
                    response.body().close(); // סגירת התגובה בכל מקרה
                }
            }
        });
    }

    private void startButtonFlashing() {
        flashTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> switchToTheLatestVersionButton.setStyle("-fx-background-color: red;")),
                new KeyFrame(Duration.seconds(1), e -> switchToTheLatestVersionButton.setStyle(""))
        );
        flashTimeline.setCycleCount(Timeline.INDEFINITE);
        flashTimeline.play();
    }

    private void stopButtonFlashing() {
        if (flashTimeline != null) {
            flashTimeline.stop();
        }
        switchToTheLatestVersionButton.setStyle("");
    }
}
