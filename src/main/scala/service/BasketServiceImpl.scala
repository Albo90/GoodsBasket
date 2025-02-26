package service

import dao.{BasketDAO, BasketDAOImpl}
import model.{BasketItem, BasketResult}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class BasketServiceImpl(input: String) extends BasketService {
  val goodsIds = input.trim.replaceAll("\\s+", " ").split(" ")
  if (goodsIds.nonEmpty && goodsIds.head != "PriceBasket") {
    throw new IllegalArgumentException("The command must follow the format: PriceBasket item1 item2 item3")
  }
  val goodsMap: mutable.Map[String, Int] = mutable.Map()
  (goodsIds.tail).foreach { good =>
    goodsMap(good) = goodsMap.getOrElse(good, 0) + 1
  }
  val basketResult = new BasketResult()

  def getBasketResult: BasketResult = basketResult

  def run(db: slick.jdbc.H2Profile.backend.JdbcDatabaseDef): Unit = {
      val dao: BasketDAO = new BasketDAOImpl(db)
      val goodKeys = goodsMap.keys.toList
      val offerList = dao.getAllValidOffers
      val offerIds = offerList.map(offer => offer.id)
      val offerDetailsList = dao.getOfferDetails(offerIds)
      val invalidOffers = offerDetailsList.filterNot(offer => goodKeys.contains(offer.goodId) && goodsMap(offer.goodId)>=offer.minQuantity ).map(offer => offer.offerId)
      val filteredOffers = offerDetailsList.filterNot(offer => invalidOffers.contains(offer.offerId))
      for (goodKey <- goodKeys) {
        val good = dao.getGoodById(goodKey)
        good match {
          case Some(optItem) =>
            val offersForGood = filteredOffers.filter(offer => offer.goodId == goodKey && offer.minQuantity <= goodsMap(goodKey))
            val price = optItem.price
            if (offersForGood.nonEmpty) {

              for (offer <- offersForGood) {
                val quantityFullPrice = goodsMap(goodKey) - offer.maxQuantity
                val items: ListBuffer[BasketItem] = ListBuffer()
                if (quantityFullPrice > 0) {
                  items.append(
                    BasketItem(offer.goodId, offer.maxQuantity, price, offer.discountPercentage))
                  items.append(
                    BasketItem(optItem.id, quantityFullPrice, price, 0))
                  basketResult.overwriteElements(optItem.id, items)
                } else {
                  items.append(
                    BasketItem(offer.goodId, goodsMap(goodKey), price, offer.discountPercentage)
                  )
                  basketResult.overwriteElements(optItem.id, items)
                }
              }
            } else {
              basketResult.addElement(BasketItem(optItem.id, goodsMap(goodKey), optItem.price, 0))
            }
          case None => println(s"Skip this item $goodKey")
        }
      }
  }
}