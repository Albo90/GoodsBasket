import dao.{BasketDAO, BasketDAOImpl}
import model._
import org.scalatest.funsuite.AnyFunSuite
import service.{BasketService, BasketServiceImpl}
import slick.jdbc.H2Profile.api._
import slick.lifted.TableQuery
import utils.Utils

import java.time.LocalDate

class PriceBasketTest extends AnyFunSuite {
  val goods = TableQuery[Goods]
  val offers = TableQuery[Offers]
  val goodsDetails = TableQuery[GoodsDetails]
  val offersDetails = TableQuery[OffersDetails]

  def beforeEach(dbName: String): slick.jdbc.H2Profile.backend.JdbcDatabaseDef = {
    System.out.println("Initiliaze DB")
    val db = Database.forURL(s"jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
    val createTableAction = DBIO.seq(
      goods.schema.createIfNotExists,
      offers.schema.createIfNotExists,
      goodsDetails.schema.createIfNotExists,
      offersDetails.schema.createIfNotExists
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
    db
  }

  test("TEST 1") {
    val db = beforeEach("test_1")
    val input = "PriceBasket Apples Milk Bread"
    val service: BasketService = new BasketServiceImpl(input)
    service.run(db)
    val basket = service.getBasketResult
    val res = basket.getTotal
    val rightRes = "Subtotal: 3.10 euro\nApples 10.0% off: 0.1 euro X quantity = 1\nTotalPrice: 3.00 euro"
    db.close()
    assert(res == rightRes)
  }

  test("TEST 2") {
    val db = beforeEach("test_2")
    val input = "PriceBasket Soup Soup Bread"
    val service: BasketService = new BasketServiceImpl(input)
    service.run(db)
    val basket = service.getBasketResult
    val res = basket.getTotal
    val rightRes = "Subtotal: 2.10 euro\nBread 50.0% off: 0.4 euro X quantity = 1\nTotalPrice: 1.70 euro"
    db.close()
    assert(res == rightRes)
  }
}
