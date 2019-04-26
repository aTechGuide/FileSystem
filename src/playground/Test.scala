package playground

object Test extends App {

  val test  = List(1,2,3,4)

  println(test)
  println(test.head)
  println(test.tail)
  println(test.init)

  val list2 = test :+ 5

  println(list2)
  println(list2.head)
  println(list2.tail)
  println(list2.init)

  val st = List("D", "A", "B")
  println(st.head)
  println(st.tail)
  println(st.init)



}
