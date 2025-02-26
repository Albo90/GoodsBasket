package utils

import slick.dbio.DBIO

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{Await, Future}

class Utils {
}

object Utils {
  def runSyncQuery[T](action: DBIO[T], conn: slick.jdbc.H2Profile.backend.JdbcDatabaseDef, timeout: FiniteDuration = 1.minutes): T = {
    val resultFuture: Future[T] = conn.run(action)
    Await.result(resultFuture, timeout)
  }

}
