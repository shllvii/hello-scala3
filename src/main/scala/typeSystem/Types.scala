package typeSystem

object Types {
  trait Record {
    type Key
    def defaultValue: Key
  }

  class StringRecord extends Record {
    override type Key = String
    override def defaultValue = ""
  }

  class IntRecord extends Record {
    override type Key = Int
    override def defaultValue = 0
  }

  // user-facing API
  def getDefaultIdentifier(record: Record): record.Key = record.defaultValue

  val aString: String = getDefaultIdentifier(new StringRecord) // a string
  val anInt: Int = getDefaultIdentifier(new IntRecord) // an int, ok

  // functions with dependent types
  val getIdentifierFunc: Record => Record#Key =
    getDefaultIdentifier // eta-expansion

  object SocialNetwork {
    // usually applied inside tooling/libraries managing data types
    // domain = all the type definitions for your business use case
    opaque type Name = String

    // API entry point #1 - companion objects, useful for factory methods
    object Name {
      def apply(str: String): Name = str
    }

    // API entry point #2 - extension methods, give you control of the methods YOU want to expose
    extension (name: Name) def length: Int = name.length // use String API

    // inside, Name <-> String
    def addFriend(person1: Name, person2: Name): Boolean =
      person1.length == person2.length // use the entire String API
  }

  // outside SocialNetwork, Name and String are NOT related
  import SocialNetwork.*
  // how can we create instances of opaque types + how to access their APIs
  // 1 - companion objects
  val aName = Name("Daniel") // ok
  // 2 - extension methods
  val nameLength = aName.length // ok because of the extension method

}
