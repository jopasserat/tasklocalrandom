package fr.isima;

import fr.isima.random.*;
import fr.isima.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jopasserat
 */
public class Main3 extends RandomSafeRunnable {

  @Override
  public void run() {

  }


  /**
   * @param args the command line arguments
   */
  public static void main(String[] argv) {
    Main3 task = new Main3();

    final int loopsToPerform = Integer.parseInt(argv[0]);
    final int callsToPerform = Integer.parseInt(argv[1]);

    double totalTime = 0;

    int taskId = 0;

    // averaging loop
    for (int i = 0; i < loopsToPerform; ++i) {
      // start timing
     long startTime = System.nanoTime();
     for (int j = 0; j < callsToPerform; ++j) {
//      while (true) {
	taskId += task.getTaskId();
      }
      // stop timing and sum
      long endTime = System.nanoTime();

      totalTime += (endTime - startTime);
    }


    System.out.println ( "(" + loopsToPerform + " x ) " + callsToPerform +
	" calls to RandomSafeRunnable.getTaskId() performed in " + 
	(totalTime / loopsToPerform)/1000000.0 + " ms" );

  }
}

