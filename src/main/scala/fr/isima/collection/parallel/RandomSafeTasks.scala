/**
 * @author jH@CKtheRipper
 */


package fr.isima.collection.parallel

import scala.collection.parallel._


/** An implementation of tasks objects based on the Java thread pooling API.
  * This tasks use RandomSafeRunnable that ensures each task to get its own independent
  * pseudorandom stream.
  * Because of RandomSafeRunnable being an abstract class and not an interface,
  * this class is a clone of ThreadPoolTasks with RandomSafeRunnable being used
  * instead of Runnable.
  * */
trait RandomSafeTasks extends ThreadPoolTasks {
  import java.util.concurrent._
  import fr.isima.util.concurrent._

  trait WrappedTask[R, +Tp] extends RandomSafeRunnable with super.WrappedTask[R, Tp] {
    // initially, this is null
    // once the task is started, this future is set and used for `sync`
    // utb: var future: Future[_] = null
   /* @volatile var owned = false
    @volatile var completed = false*/

    override def start() = synchronized {
      // debuglog("Starting " + body)
      // utb: future = executor.submit(this)
      executor.synchronized {
        incrTasks()
        executor.submit(this)
      }
    }
    override def sync() = synchronized {
      // debuglog("Syncing on " + body)
      // utb: future.get()
      executor.synchronized {
        val coresize = executor.getCorePoolSize
        if (coresize < totaltasks) {
          executor.setCorePoolSize(coresize + 1)
          //assert(executor.getCorePoolSize == (coresize + 1))
        }
      }
      while (!completed) this.wait
    }
    override def tryCancel() = synchronized {
      // utb: future.cancel(false)
      if (!owned) {
        // debuglog("Cancelling " + body)
        owned = true
        true
      } else false
    }
    override def run() = {
      // utb: compute
      var isOkToRun = false
      synchronized {
        if (!owned) {
          owned = true
          isOkToRun = true
        }
      }
      if (isOkToRun) {
        // debuglog("Running body of " + body)
        compute()
      } else {
        // just skip
        // debuglog("skipping body of " + body)
      }
    }
    override def release() = synchronized {
      //println("releasing: " + this + ", body: " + this.body)
      completed = true
      executor.synchronized {
        decrTasks()
      }
      this.notifyAll
    }
  }

/*  protected def newWrappedTask[R, Tp](b: Task[R, Tp]): WrappedTask[R, Tp] */

  override val environment: RandomSafeThreadPoolExecutor
  override def executor = environment.asInstanceOf[RandomSafeThreadPoolExecutor]
  override def queue = executor.getQueue.asInstanceOf[LinkedBlockingQueue[Runnable]]
  /*@volatile var totaltasks = 0*/

  private def incrTasks() = synchronized {
    totaltasks += 1
  }

  private def decrTasks() = synchronized {
    totaltasks -= 1
  }

  /*def execute[R, Tp](task: Task[R, Tp]): () => R = {
    val t = newWrappedTask(task)

    // debuglog("-----------> Executing without wait: " + task)
    t.start()

    () => {
      t.sync()
      t.body.forwardThrowable
      t.body.result
    }
  }

  def executeAndWaitResult[R, Tp](task: Task[R, Tp]): R = {
    val t = newWrappedTask(task)

    // debuglog("-----------> Executing with wait: " + task)
    t.start()

    t.sync()
    t.body.forwardThrowable
    t.body.result
  }

  def parallelismLevel = ThreadPoolTasks.numCores*/

}

object RandomSafeTasks {
  import java.util.concurrent._
  import fr.isima.util.concurrent._

  val numCores = Runtime.getRuntime.availableProcessors

  val tcount = new atomic.AtomicLong(0L)

  val defaultThreadPool = new RandomSafeThreadPoolExecutor(
    numCores,
    Int.MaxValue,
    60L, TimeUnit.MILLISECONDS,
    new LinkedBlockingQueue[Runnable],
    new ThreadFactory {
      def newThread(r: Runnable) = {
        val t = new Thread(r)
        t.setName("pc-thread-" + tcount.incrementAndGet)
        t.setDaemon(true)
        t
      }
    },
    new ThreadPoolExecutor.CallerRunsPolicy
  )
}
