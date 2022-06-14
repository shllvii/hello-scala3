package typeSystem

import com.sourcegraph.semanticdb_javac.Semanticdb.StructuralType

object MoreTypes {
  val three: 3 = 3

  class Animal
  trait Carnivore
  class Crocodile extends Animal with Carnivore

  val carnivoerAnimal: Animal & Carnivore = new Crocodile

  type Maybe[T] = T | Null
  def handleMaybe(someValue: Maybe[String]): Int =
    if (someValue != null) someValue.length
    else 0

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // self-type: whoever implements Singer MUST also implement Instrumentalist
    // ^^ name can be anything, usually called "self"
    // DO NOT confuse this with a lambda
    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???
    override def sing(): Unit = ???
  }

  // cake pattern
  trait ComponentLayer1 {
    def actionLayer1(x: Int): String
  }
  trait ComponentLayer2 { self: ComponentLayer1 =>
    def actionLayer2(x: String): Int
  }

  trait Application { self: ComponentLayer1 with ComponentLayer2 => }

  trait Stats extends ComponentLayer1

  // layer 2 - compose
  trait Analytics extends ComponentLayer2 with Stats

  // layer 3 - application
  trait AnalyticsApp extends Application with Analytics

  object FBP {
    trait Animal[A <: Animal[A]] { // recursive type, F-bounded polymorphism
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] {
      override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
    }

    class Dog extends Animal[Dog] {
      override def breed = List(new Dog, new Dog, new Dog)
    }

    // mess up FBP
    class Crocodile extends Animal[Dog] {
      override def breed = ??? // list of dogs
    }
  }

  // example: some ORM libraries
  trait Entity[E <: Entity[E]]
  // example: Java sorting library
  class Person extends Comparable[Person] { // FPB
    override def compareTo(o: Person) = ???
  }

  // FBP + self types
  object FBPSelf {
    trait Animal[A <: Animal[A]] { self: A => // the self type is important
      def breed: List[Animal[A]]
    }

    class Cat extends Animal[Cat] { // Cat == Animal[Cat]
      override def breed: List[Animal[Cat]] = List(new Cat, new Cat)
    }

    class Dog extends Animal[Dog] {
      override def breed = List(new Dog, new Dog, new Dog)
    }

    // I can go one level deeper
    trait Fish extends Animal[Fish]
    class Cod extends Fish {
      override def breed = List(new Cod, new Cod)
    }

    class Shark extends Fish {
      override def breed: List[Animal[Fish]] = List(new Cod)
    }

    // solution level 2
    trait FishL2[A <: FishL2[A]] extends Animal[FishL2[A]] { self: A => }
    class Tuna extends FishL2[Tuna] {
      override def breed = List(new Tuna)
    }
    // not ok
    //    class Swordfish extends FishL2[Swordfish] {
    //      override def breed = List(new Tuna)
    //    }
  }

  object StructuralType {
    type SoundMaker = { // structural type
      def makeSound(): Unit
    }

    class Dog {
      def makeSound(): Unit = println("bark!")
    }

    class Car {
      def makeSound(): Unit = println("vroom!")
    }

    val dog: SoundMaker = new Dog // ok
    val car: SoundMaker = new Car
    // compile-time duck typing

    // type refinements
    abstract class Animal {
      def eat(): String
    }

    type WalkingAnimal = Animal { // refined type
      def walk(): Int
    }
  }

}
