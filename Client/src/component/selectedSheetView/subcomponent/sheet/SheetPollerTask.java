package component.selectedSheetView.subcomponent.sheet;

import javafx.application.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.util.Duration;
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

        try (Response response = HttpClientUtil.HTTP_CLIENT.newCall(request).execute()) { // קריאה סינכרונית
            if (response.isSuccessful()) {
                if (!isFlashing) {
                    Platform.runLater(this::startButtonFlashing);
                    isFlashing = true;
                }
            } else if (isFlashing) {
                Platform.runLater(this::stopButtonFlashing);
                isFlashing = false;
            }
        } catch (IOException e) {
            System.err.println("Failed to poll for updates: " + e.getMessage());
        }
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
