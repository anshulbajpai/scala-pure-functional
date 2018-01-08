package categorytheory.demo.support

import categorytheory.core.{Id, ~}
import categorytheory.datatypes.Free
import categorytheory.datatypes.Free.liftF

trait OrdersLanguage {

  sealed trait Order[A]

  type Response = String
  type Symbol = String

  case class Buy(stock: Symbol, quantity: Int) extends Order[Response]

  case class Sell(stock: Symbol, quantity: Int) extends Order[Response]

  case class ListStocks() extends Order[List[Symbol]]

  type OrdersF[A] = Free[Order, A]

  def buy(stock: Symbol, quantity: Int): OrdersF[Response] = liftF[Order, Response](Buy(stock, quantity))
  def sell(stock: Symbol, quantity: Int): OrdersF[Response] = liftF[Order, Response](Sell(stock, quantity))
  def listStocks(): OrdersF[List[Symbol]] = liftF[Order, List[Symbol]](ListStocks())


  val orderPrinter: Order ~ Id = new (Order ~ Id) {
    override def apply[A](fa: Order[A]): Id[A] = fa match {
      case Buy(stock, quantity) =>
        println(s"Buying $quantity of $stock")
        "ok"
      case Sell(stock, quantity) =>
        println(s"Selling $quantity of $stock")
        "ok"
      case ListStocks() =>
        println("Listing stocks - APPLE, GOOGLE")
        List("APPLE", "GOOGLE")
    }
  }

  type ErrorOr[A] = Either[String, A]

  val betterOrderPrinter: Order ~ ErrorOr = new (Order ~ ErrorOr) {
    override def apply[A](fa: Order[A]): ErrorOr[A] = fa match {
      case Buy(stock, quantity) => Right(s"Buy - $stock - $quantity")
      case Sell(stock, quantity) => Left("Why are you selling that?")
      case ListStocks() => Right(List("APPLE", "GOOGLE"))
    }

  }
}

