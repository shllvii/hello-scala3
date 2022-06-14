package contextAbstraction

object Organize {
  case class Person(name: String, age: Int)
  object PersonGivens {
    given ageOrdering: Ordering[Person] with
      override def compare(x: Person, y: Person) = y.age - x.age

    extension (p: Person)
      def greet(): String = s"Heya, I'm ${p.name}, I'm so glad to meet you!"
  }

  import PersonGivens.{given Ordering[Person]}

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 67)
  )
  val sortedPersons = persons.sorted
  // a - import explicitly
  // import PersonGivens.ageOrdering

  // b - import a given for a particular type

  def main(args: Array[String]): Unit = {
    println(sortedPersons)
    import PersonGivens.* // includes extension methods
    println(Person("Daniel", 99).greet())
  }
}
