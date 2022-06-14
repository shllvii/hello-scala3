package contextAbstraction

object TypeClass {
  trait MyTypeClass[T] {
    def action(
        value: T
    ): String // can have multiple methods; adapt signatures to your needs
  }
  // 2 - type class instances
  given intInstance: MyTypeClass[Int] with
    override def action(value: Int) = value.toString
  // same for other types you want to support

  // 3 - user-facing API
  object MyTypeClass {
    // often similar to what the type class definition offers
    def action[T](value: T)(using instance: MyTypeClass[T]): String =
      instance.action(value)
    // often expose a method to retrieve the current given instance for a type (similar to summon)
    def apply[T](using instance: MyTypeClass[T]): MyTypeClass[T] = instance
  }

  // 4 - expressiveness through extension methods
  object MyTypeClassSyntax {
    extension [T](value: T)
      def action(using instance: MyTypeClass[T]): String =
        instance.action(value)
  }
}
