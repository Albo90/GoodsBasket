package model

import scala.collection.mutable.ListBuffer
import scala.math.BigDecimal.RoundingMode

class BasketResult {
  var basketList: ListBuffer[BasketItem] = ListBuffer()

  def addElement(elem: BasketItem): Unit = {
    basketList += elem
  }

  def overwriteElements(goodItem: String, list: ListBuffer[BasketItem]): Unit = {
    val basketListCopy = basketList.clone()
    val actualPrice = basketListCopy.filter(b => b.getGood == goodItem).
        foldLeft(0.0) { (acc, b) => acc + (b.getPrice * b.getSize) - (b.getPrice * b.getSize * b.getPercentageDiscount / 100) }
    val newPrice = list.
        foldLeft(0.0) { (acc, b) => acc + (b.getPrice * b.getSize) - (b.getPrice * b.getSize * b.getPercentageDiscount / 100) }
    if (newPrice < actualPrice || actualPrice == 0) {
        basketList=basketList.filterNot(b => b.getGood == goodItem)
        basketList ++= list
      }
  }

  def getTotal: String = {
    var total = 0.0
    var totalDiscounted = total
    var stringPercentage = ""
    for (item <- basketList) {
      val totalPriceItem = item.getPrice * item.getSize
      if (item.getPercentageDiscount > 0) {
        val strPercentage = s"${item.getGood} ${item.getPercentageDiscount}% off: ${item.getPrice*item.getPercentageDiscount/100} euro X quantity = ${item.getSize}\n"
        stringPercentage = strPercentage + stringPercentage
      }
      total += totalPriceItem
      totalDiscounted += (totalPriceItem - totalPriceItem * item.getPercentageDiscount / 100)
    }
    val roundTotal = BigDecimal(total).setScale(2, RoundingMode.HALF_UP)
    val roundTotalDiscounted = BigDecimal(totalDiscounted).setScale(2, RoundingMode.HALF_UP)
    if (stringPercentage.isEmpty) {
      stringPercentage = "(No Offers available)\n"
    }
    val res = s"Subtotal: $roundTotal euro\n${stringPercentage}TotalPrice: $roundTotalDiscounted euro"
    res
  }

}
