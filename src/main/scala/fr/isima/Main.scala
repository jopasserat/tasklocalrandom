package fr.isima

import scala.collection.parallel._
import fr.isima.collection.parallel._
import fr.isima.random.TaskLocalRandom

object Main extends App {
  val pc = (1 to 10000).par

  pc.tasksupport = new RandomSafeTaskSupport()

  pc.map (
    i => {
//      var rng = new TaskLocalRandom(this)
      i * i
//      var rng = new TaskLocalRandom(this)
    }
    )
}
