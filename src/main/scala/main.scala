import scala.io.StdIn._
import dao.{BasketDAO, BasketDAOImpl}
import model.{Good, GoodDetails, Goods, GoodsDetails, Offer, OfferDetails, Offers, OffersDetails}
import service.{BasketService, BasketServiceImpl}
import slick.jdbc.H2Profile.api._
import utils._

import java.time.LocalDate
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.control.Breaks.break
object main {
  def main(args: Array[String]): Unit = {
    val db = Database.forURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    try {
      val goods = TableQuery[Goods]
      val offers = TableQuery[Offers]
      val goodsDetails = TableQuery[GoodsDetails]
      val offersDetails = TableQuery[OffersDetails]
      val createTableAction = DBIO.seq(
        goods.schema.create,
        offers.schema.create,
        goodsDetails.schema.create,
        offersDetails.schema.create
      )
      Utils.runSyncQuery(createTableAction, db)
      val dao: BasketDAO = new BasketDAOImpl(db)
      val good1 = Good("Bakery", null, null)
      dao.addGood(good1)
      val good2 = Good("Dairy", null, null)
      dao.addGood(good2)
      val good3 = Good("Fruit", null, null)
      dao.addGood(good3)
      val good4 = Good("Cleaning", null, null)
      dao.addGood(good4)
      val goodDetails1 = GoodDetails("Bread", "Bakery", null, 0.8, "loaf", null, null)
      dao.addGoodDetails(goodDetails1)
      val goodDetails2 = GoodDetails("Milk", "Dairy", null, 1.3, "bottle", null, null)
      dao.addGoodDetails(goodDetails2)
      val goodDetails3 = GoodDetails("Apples", "Fruit", null, 1.0, "bag", null, null)
      dao.addGoodDetails(goodDetails3)
      val goodDetails4 = GoodDetails("Soup", "Cleaning", null, 0.65, "tin", null, null)
      dao.addGoodDetails(goodDetails4)
      val offer1 = Offer("offer_1", null, LocalDate.now(), LocalDate.now(), null)
      dao.addOffer(offer1)
      val offer2 = Offer("offer_2", null, LocalDate.now(), LocalDate.now(), null)
      dao.addOffer(offer2)
      val offerDetails1 = OfferDetails("Apples", "offer_1", 1, 999, 10.0)
      dao.addOffersDetails(offerDetails1)
      val offerDetails2 = OfferDetails("Soup", "offer_2", 2, 2, 0.0)
      dao.addOffersDetails(offerDetails2)
      val offerDetails3 = OfferDetails("Bread", "offer_2", 1, 1, 50.0)
      dao.addOffersDetails(offerDetails3)
      while (true) {
        println("Insert your Articles or Type exit to close the program")
        val input = readLine()
        if (input =="exit") System.exit(0)
        val service: BasketService = new BasketServiceImpl(input)
        service.run(db)
        val basket = service.getBasketResult
        println(basket.getTotal)
        println("\n")
      }
    }  catch {
        case ex: Exception =>
          println(s"An error occurred: ${ex.getMessage}")
          throw ex
      }
      finally {
        db.close()
      }
    }
}
