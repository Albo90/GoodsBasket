package dao

import model.{Good, GoodDetails, Offer, OfferDetails}

trait BasketDAO {
  def addGood(good: Good): Boolean

  def addGoodDetails(goodsDetails: GoodDetails): Boolean

  def addOffer(offer: Offer): Boolean

  def addOffersDetails(offersDetails: OfferDetails): Boolean

  def getGoodById(id: String): Option[GoodDetails]

  def getAllValidOffers: List[Offer]

  def getOfferDetails(offerIds: List[String]): List[OfferDetails]

}
