package service

import model.BasketResult

trait BasketService {
  def run(db: slick.jdbc.H2Profile.backend.JdbcDatabaseDef): Unit

  def getBasketResult: BasketResult
}
