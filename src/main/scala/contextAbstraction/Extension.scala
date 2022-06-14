package contextAbstraction

object Extension {
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }

  extension [A](list: List[A]) {
    def combineAll(using combinator: Semigroup[A]): A =
      list.reduce(combinator.combine)
  }

  given IntCombinator: Semigroup[Int] with
    override def combine(x: Int, y: Int): Int = x + y

  val aList = List(1, 2, 3, 4)
  val aSum = aList.combineAll

  given StringCombinator: Semigroup[String] = new Semigroup[String] {
    override def combine(x: String, y: String): String = x + y
  }
  val sList = List("a", "b", "c", "d")
  val sSum = sList.combineAll

}
