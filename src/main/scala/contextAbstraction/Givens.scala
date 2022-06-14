package contextAbstraction

object Givens {
  val aList = List(4, 3, 2, 1)
  val anOrderedList = aList.sorted

  given descendingOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  val anInverseOrderedList = aList.sorted(descendingOrdering)

  case class Person(name: String, age: Int)
  val people = List(Person("Alice", 29), Person("Sarah", 34), Person("Jim", 23))
  given personOrdering: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int = x.name compareTo y.name
  }
  val sortedPeople = people.sorted

  trait Combinator[A] {
    def combine(x: A, y: A): A
  }

  def combineAll[A](list: List[A])(using combinator: Combinator[A]): A =
    list.reduce(combinator.combine)

  given intCombinator: Combinator[Int] with {
    override def combine(x: Int, y: Int) = x + y
  }

  val firstSum = combineAll(
    List(1, 2, 3, 4)
  ) // (intCombinator) <-- passed automatically
  // val combineAllPeople = combineAll(people) // does not compile - no Combinator[Person] in scope

  // context bound
  def combineInGroupsOf3_v2[A: Combinator](
      list: List[A]
  ): List[A] = // A : Combinator => there is a given Combinator[A] in scope
    list
      .grouped(3)
      .map(group =>
        combineAll(group) /* given Combinator[A] passed by the compiler */
      )
      .toList

  val myCombinator = new Combinator[Int] {
    override def combine(x: Int, y: Int) = x * y
  }
  val listProduct = combineAll(List(1, 2, 3, 4))(using myCombinator)

}
