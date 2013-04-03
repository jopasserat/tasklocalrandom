package fr.isima;

import fr.isima.random.*;
import fr.isima.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jopasserat
 */
public class Main4 extends RandomSafeRunnable {

  @Override
    public void run() {

      TaskLocalRandom rng = new TaskLocalRandom(this);

//      for (int i = 0; i < 1000000; ++i) {
      while(true) {
	rng.next();
      }
    }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); ++i) {
      Runnable task = new Main4();
      executor.execute(task);
    }

    executor.shutdown();
    while (!executor.isTerminated()) {}
  }
}
