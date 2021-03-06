import cats.data.Reader

case class Cat(name: String, favoriteFood: String)

// defined class Cat
val catName: Reader[Cat, String] = Reader(cat => cat.name)
// catName: cats.data.Reader[Cat,String] = Kleisli(<function1>)
catName.run(Cat("Garfield", "lasagne"))
// res0: cats.Id[String] = Garfield
val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello ${name}")
greetKitty.run(Cat("Heathcliff", "junk food"))
// res1: cats.Id[String] = Hello Heathcliff

val feedKitty: Reader[Cat, String] =
  Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")
val greetAndFeed: Reader[Cat, String] =
  for {
    greet <- greetKitty
    feed <- feedKitty
  } yield s"$greet. $feed."
greetAndFeed(Cat("Garfield", "lasagne"))
  // res3: cats.Id[String] = Hello Garfield. Have a nice bowl of lasagne. greetAndFeed(Cat("Heathcliff", "junk food"))
// res4: cats.Id[String] = Hello Heathcliff. Have a nice bowl of junk food.