import fr.isima.random.*;
import fr.isima.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jopasserat
 */
public class Main2 extends RandomSafeRunnable {

    @Override
    public void run() {

        TaskLocalRandom rng = new TaskLocalRandom(this);

            System.out.println("Task[" + this.getTaskId() +
                                "] from Thread[" + Thread.currentThread().getId() +
                                "] {" + rng.Cg[0] + ", " + rng.Cg[1] + ", " + rng.Cg[2] + ", " + rng.Cg[3] + ", " + rng.Cg[4] + ", " + rng.Cg[5] + "}");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() );
        
        long seed[] = {12345, 12345,12345,12345,12345,12346};

	TaskLocalRandom.setPackageSeed(seed);
        for (int i = 0; i < 5; ++i) {
            Runnable task = new Main2();
            executor.execute(task);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {}
    }
}
