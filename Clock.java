import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class Clock {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
    private volatile String currentTime;

    // Thread to update the time
    Thread timeUpdater = new Thread(() -> {
        while (true) {
            currentTime = formatter.format(LocalDateTime.now());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    });

    // Thread to display the time
    Thread timeDisplayer = new Thread(() -> {
        while (true) {
            // Check if currentTime is already updated and not null, then display it
            if (currentTime != null) {
                System.out.println(currentTime);
            }
            try {
                // Sleep for 1 second before updating the time again
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                // If the thread is interrupted, preserve the interrupt status
                Thread.currentThread().interrupt();
            }
        }
    });

    public void start() {
        // Set priorities for the threads
        // The thread that updates the time has the minimum priority
        timeUpdater.setPriority(Thread.MIN_PRIORITY);
        // The thread that displays the time has the maximum priority
        timeDisplayer.setPriority(Thread.MAX_PRIORITY);

        // Start the threads
        // This will call the run() method of the threads
        timeUpdater.start();
        timeDisplayer.start();
    }

    public static void main(String[] args) {
        // Create a new Clock object and start it
        new Clock().start();
    }
}
