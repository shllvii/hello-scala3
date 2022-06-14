package contextAbstraction
import scala.concurrent.{ExecutionContext, Future}

object ContextFunction {

  def methodWithContextArguments(nonContextArg: Int)(using
      nonContextArg2: String
  ): String = ???

  val functionWithContextArgument: Int => String ?=> String =
    methodWithContextArguments

  val incrementAsync: ExecutionContext ?=> Int => Future[Int] = x =>
    Future(x + 1)

  def main(args: Array[String]): Unit = {}

}
