package v1.post

/**
  * Created by Daniel on 27.06.2017.
  */
import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.PostgresProfile

case class Thing(s: String)

class Test @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[PostgresProfile] {
  import profile.api._

  private val Things = TableQuery[ThingTable]

  def all(): Future[Seq[Thing]] = db.run(Things.result)

  def insert(t: Thing): Future[Unit] = db.run(Things += t).map { _ => () }

  private class ThingTable(tag: Tag) extends Table[Thing](tag, "pg_database") {

    def name = column[String]("datname")

    def * = (name) <> (Thing.apply, Thing.unapply)
  }
}