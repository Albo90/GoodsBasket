package model

import slick.jdbc.PostgresProfile.api._

import java.time.LocalDate

case class Offer(id: String, description: Option[String], startDate: LocalDate, endDate: LocalDate, notes: Option[String])

class Offers(tag: Tag) extends Table[Offer](tag, "Offers") {
  def * = (id, description, startDate, endDate, notes) <> (Offer.tupled, Offer.unapply)

  def id = column[String]("id", O.PrimaryKey)

  def description = column[Option[String]]("description")

  def startDate = column[LocalDate]("startDate")

  def endDate = column[LocalDate]("endDate")

  def notes = column[Option[String]]("notes")
}
