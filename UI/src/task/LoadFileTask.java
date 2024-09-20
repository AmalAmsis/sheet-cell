package task;

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
            // Stage 1: Initiating file fetch
            updateMessage("Initiating file fetch...");
            updateProgress(0.1, 1); // 10% progress
            sleepForAWhile(SLEEP_TIME); // Simulating delay

            // Stage 2: Verifying file location
            updateMessage("Verifying file location...");
            updateProgress(0.3, 1); // 30% progress
            sleepForAWhile(SLEEP_TIME); // Simulating delay

            // Stage 3: Preparing file for loading
            updateMessage("Preparing file for loading...");
            updateProgress(0.5, 1); // 50% progress
            sleepForAWhile(SLEEP_TIME); // Simulating delay

            // Stage 4: Loading the file
            updateMessage("Loading the file...");
            updateProgress(0.8, 1); // 80% progress
            sleepForAWhile(SLEEP_TIME);
            Platform.runLater(() -> loadFileFunction.accept(fileName)); // Actual file loading

            // Stage 5: Finalizing the loading process
            updateMessage("Finalizing the loading process...");
            updateProgress(1, 1); // 100% progress
            sleepForAWhile(SLEEP_TIME);

            // Call the success handler
            Platform.runLater(() -> {
                updateMessage("Success: File loaded successfully!");
                loadingSuccessFunction.accept(fileName);
            });

            return true; // Task succeeded

        } catch (Exception e) {
            // Handle any errors that occur during the loading process
            updateMessage("Error: Failed to load the file. " + e.getMessage());
            return false;
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

