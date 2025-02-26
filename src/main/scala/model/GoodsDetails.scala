package model

import slick.jdbc.PostgresProfile.api._

case class GoodDetails(id: String, goodType: String, brand: Option[String], price: Double, metric: String, description: Option[String], notes: Option[String])

class GoodsDetails(tag: Tag) extends Table[GoodDetails](tag, "GoodsDetails") {
  def goodDet = foreignKey("fk_good_details", goodType, TableQuery[Goods])(_.id)

  def goodType = column[String]("goodType")

  def * = (id, goodType, brand, price, metric, description, notes) <> (GoodDetails.tupled, GoodDetails.unapply)

  def id = column[String]("id", O.PrimaryKey)

  def brand = column[Option[String]]("brand")

  def price = column[Double]("price")

  def metric = column[String]("metric")

  def description = column[Option[String]]("description")

  def notes = column[Option[String]]("notes")
}
