package typeSystem

object Inheritance {
  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Stream[T] {
    def foreach(f: T => Unit): Unit
  }

  trait Closeable {
    def close(status: Int): Unit
  }

  // class MyDataStream extends Writer[String] with Stream[String] with Closeable { ... }

  def processStream[T](
      stream: Writer[T] with Stream[T] with Closeable
  ): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem
  trait Animal { def name: String }
  trait Lion extends Animal { override def name = "Lion" }
  trait Tiger extends Animal { override def name = "Tiger" }
  class Liger extends Lion with Tiger

  def demoLiger(): Unit = {
    val liger = new Liger
    println(liger.name)
  }

  def main(args: Array[String]): Unit = {
    demoLiger()
  }

  object HigherKindedTypes {
    class HigherKindedType[F[_]]
    val hkExample = new HigherKindedType[List]

    trait Functor[F[_]] {
      def map[A, B](fa: F[A])(f: A => B) : F[B]
    }
    // 1 - TC definition
    trait Monad[F[_]] extends Functor[F] {
      def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    }

    // 2 - TC instance(s)
    given magicList: Monad[List] with {
      override def map[A, B](list: List[A])(f: A => B) = list.map(f)
      override def flatMap[A, B](list: List[A])(f: A => List[B]) =
        list.flatMap(f)
    }

    // 3 - "user-facing" API
    def combine[F[_], A, B](fa: F[A], fb: F[B])(using
        magic: Monad[F]
    ): F[(A, B)] =
      magic.flatMap(fa)(a => magic.map(fb)(b => (a, b)))
    // listA.map(a => listB.map(b => (a,b))

    extension [F[_], A](container: F[A])(using magic: Monad[F])
      def flatMap[B](f: A => F[B]): F[B] = magic.flatMap(container)(f)
  }
}
