package fr.isima.collection.parallel

import scala.collection.parallel._
import fr.isima.util.concurrent.RandomSafeThreadPoolExecutor


class RandomSafeTaskSupport(val environment: RandomSafeThreadPoolExecutor = RandomSafeTasks.defaultThreadPool)
  extends TaskSupport with AdaptiveWorkStealingThreadPoolTasks
