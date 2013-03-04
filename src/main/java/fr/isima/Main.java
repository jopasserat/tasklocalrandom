import fr.isima.random.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jopasserat
 */
public class Main extends RandomSafeRunnable {

    @Override
    public void run() {

        TaskLocalRandom rng = new TaskLocalRandom(this);

        for (int i = 0; i < 5; ++i) {
            System.out.println("Task[" + this.getTaskId() +
                                "] from Thread[" + Thread.currentThread().getId() +
                                "] {" + i + "} = " + rng.next());
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        
        for (int i = 0; i < 100; ++i) {
            Runnable task = new Main();
            executor.execute(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}
    }
}
