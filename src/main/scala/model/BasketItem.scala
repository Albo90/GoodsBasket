package model

case class BasketItem(good: String, size: Int, price: Double, percentageDiscount: Double) {

  def getGood: String = good

  def getSize: Int = size

  def getPrice: Double = price

  def getPercentageDiscount: Double = percentageDiscount

}
