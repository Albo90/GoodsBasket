package dao

import model._
import slick.jdbc.PostgresProfile.api._
import slick.lifted.TableQuery
import utils._

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global

class BasketDAOImpl(db: slick.jdbc.H2Profile.backend.JdbcDatabaseDef) extends BasketDAO {
  val goods = TableQuery[Goods]
  val goodsDetails = TableQuery[GoodsDetails]
  val offers = TableQuery[Offers]
  val offersDetails = TableQuery[OffersDetails]

  def addGood(goodItem: Good): Boolean = {
    val insertQuery = goods += goodItem
    Utils.runSyncQuery(insertQuery, db)
    true

  }

  def addGoodDetails(goodDetailsItem: GoodDetails): Boolean = {
    val insertQuery = goodsDetails += goodDetailsItem
    Utils.runSyncQuery(insertQuery, db)
    true
  }

  def addOffer(offerItem: Offer): Boolean = {
    val insertQuery = offers += offerItem
    Utils.runSyncQuery(insertQuery, db)
    true
  }

  def addOffersDetails(offerDetailsItem: OfferDetails): Boolean = {
    val insertQuery = offersDetails += offerDetailsItem
    Utils.runSyncQuery(insertQuery, db)
    true
  }

  def getGoodById(id: String): Option[GoodDetails] = {
    val query = goodsDetails.filter(good => good.id === id).result
    val resGood = Utils.runSyncQuery(query, db).headOption
    resGood
  }

  def getAllValidOffers: List[Offer] = {
    val today = LocalDate.now()
    val query = offers.filter(offer => offer.startDate <= today && offer.endDate >= today).result.map(_.toList)
    val resOffers = Utils.runSyncQuery(query, db)
    resOffers
  }

  def getOfferDetails(offerIds: List[String]): List[OfferDetails] = {
    val query = offersDetails.filter(off => off.offerId.inSet(offerIds.toSet)).result.map(_.toList)
    val resOffersDetails = Utils.runSyncQuery(query, db)
    resOffersDetails
  }

}