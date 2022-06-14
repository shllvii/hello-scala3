package typeSystem

object Variance {
  class Animal
  class Dog(name: String) extends Animal

  // Variance question for List: if Dog extends Animal, then should a List[Dog] "extend" List[Animal]?

  // for List, YES - List is COVARIANT
  val lassie = new Dog("Lassie")
  val hachi = new Dog("Hachi")
  val laika = new Dog("Laika")

  val anAnimal: Animal = lassie // ok, Dog <: Animal
  val myDogs: List[Animal] = List(
    lassie,
    hachi,
    laika
  ) // ok - List is COVARIANT: a list of dogs is a list of animals

  class MyList[+A]
  val aListOfAnimals: MyList[Animal] = new MyList[Dog]

  trait Vet[-A] {
    def heal(animal: A): Boolean
  }

  val myVet: Vet[Dog] = new Vet[Animal] {
    override def heal(animal: Animal): Boolean = {
      println("Hey there, you're all good...")
      return true
    }
  }

  val healLaika = myVet.heal(laika)

  class Vehicle
  class Car extends Vehicle
  class SuperCar extends Car

  class RepairShop[-A <: Vehicle] {
    def repair[B <: A](vehicle: B): B = vehicle
  }

  val myRepairShop: RepairShop[Car] = new RepairShop[Vehicle]
  val myBeatupVW = new Car
  val freshCar = myRepairShop.repair(myBeatupVW) // works, returns a car
  val damagedFerrari = new SuperCar
  val freshFerrari =
    myRepairShop.repair(damagedFerrari) // works, returns a Supercar

}
