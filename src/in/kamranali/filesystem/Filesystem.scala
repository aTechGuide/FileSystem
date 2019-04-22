package in.kamranali.filesystem

import java.util.Scanner

import in.kamranali.commands.Command
import in.kamranali.files.Directory

object Filesystem extends App {

  val root = Directory.ROOT
  var state = State(root, root)

  val scanner = new Scanner(System.in) // `System.in` is standard I/P
  while (true) {
    state.show
    val input = scanner.nextLine()
    state = Command.from(input).apply(state)
  }

}
