package task;

import component.subcomponent.popup.errormessage.ErrorMessage;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class LoadFileTask extends Task<Boolean> {

    private static final long SLEEP_TIME = 1000; // Simulated time for each loading stage
    private final String fileName;
    private final Consumer<String> loadFileFunction;
    private final Consumer<String> loadingSuccessFunction;

    /**
     * Constructor for LoadFileTask.
     * @param fileName The name of the file to be loaded.
     * @param loadFileFunction A function that handles the actual file loading process.
     * @param loadingSuccessFunction A function to be called once the file has been loaded successfully.
     */
    public LoadFileTask(String fileName, Consumer<String> loadFileFunction, Consumer<String> loadingSuccessFunction) {
        this.fileName = fileName;
        this.loadFileFunction = loadFileFunction;
        this.loadingSuccessFunction = loadingSuccessFunction;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            // Simulate loading stages with messages and progress updates
            updateMessage("Initiating file fetch...");
            updateProgress(0.1, 1);
            sleepForAWhile(SLEEP_TIME);

            updateMessage("Verifying file location...");
            updateProgress(0.3, 1);
            sleepForAWhile(SLEEP_TIME);

            updateMessage("Preparing file for loading...");
            updateProgress(0.5, 1);
            sleepForAWhile(SLEEP_TIME);

            // Stage 4: Loading the file outside of Platform.runLater
            updateMessage("Loading the file...");
            updateProgress(0.8, 1);
            sleepForAWhile(SLEEP_TIME);

            // Load the file directly in the task, not in runLater
            loadFileFunction.accept(fileName);

            // Finalizing stage
            updateMessage("Finalizing the loading process...");
            updateProgress(1, 1);
            sleepForAWhile(SLEEP_TIME);

            // Handle success
            Platform.runLater(() -> {
                updateMessage("Success: File loaded successfully!");
                loadingSuccessFunction.accept(fileName);
            });

            return true; // Task succeeded

        } catch (Exception e) {
            // Catch the exception and ensure the task fails
            updateMessage("Error: " + e.getMessage());
            throw e;  // Rethrow to ensure task failure is handled
        }
    }





    /**
     * Simulates sleep to simulate the loading process for each stage.
     * @param sleepTime The duration of the sleep in milliseconds.
     */
    private void sleepForAWhile(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // If interrupted, reset the thread's interrupt status
        }
    }

//    @Override
//    protected void failed() {
//        super.failed();
//        updateMessage("Task failed: Could not load the file.");
//    }

//    @Override
//    protected void succeeded() {
//        super.succeeded();
//        updateMessage("Task completed successfully.");
//    }

    @Override
    protected void cancelled() {
        super.cancelled();
        updateMessage("Task was cancelled.");
    }
}

