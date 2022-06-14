package contextAbstraction

import scala.language.implicitConversions

object ImplicitConversions {
  case class Person(name: String) {
    def greet(): String = s"Hi, I'm $name, how are you?"
  }

  given string2Person: Conversion[String, Person] with
    override def apply(x: String): Person = Person(x)

  val danialGreet = "Danial".greet()

}
