package model

import slick.jdbc.PostgresProfile.api._

case class Good(id: String, description: Option[String], notes: Option[String])

class Goods(tag: Tag) extends Table[Good](tag, "Goods") {
  def * = (id, description, notes) <> (Good.tupled, Good.unapply)

  def id = column[String]("id", O.PrimaryKey)

  def description = column[Option[String]]("description")

  def notes = column[Option[String]]("notes")
}
