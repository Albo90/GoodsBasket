package model

import slick.jdbc.PostgresProfile.api._

case class OfferDetails(goodId: String, offerId: String, minQuantity: Int, maxQuantity: Int, discountPercentage: Double)

class OffersDetails(tag: Tag) extends Table[OfferDetails](tag, "OffersDetails") {
  def goodOfferDet = foreignKey("fk_good_offer_det", goodId, TableQuery[GoodsDetails])(_.id)

  def offerDet = foreignKey("fk_offer_det", offerId, TableQuery[Offers])(_.id)

  def * = (goodId, offerId, minQuantity, maxQuantity, discountPercentage) <> (OfferDetails.tupled, OfferDetails.unapply)

  def goodId = column[String]("goodId")

  def offerId = column[String]("offerId")

  def minQuantity = column[Int]("minQuantity")

  def maxQuantity = column[Int]("maxQuantity")

  def discountPercentage = column[Double]("discountPercentage")
}
