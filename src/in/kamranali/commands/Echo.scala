package in.kamranali.commands
import in.kamranali.files.{Directory, File}
import in.kamranali.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {

  override def apply(state: State): State = {

    /*
    1. If no args then state
    2. If one arg then print to console
    3. If multiple args i.e. echo abc >> data
        if operator = '>' echo to a file
        if operator = '>>' append to a file
     */

    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0)) //
    else {

      val operator = args(args.length - 2)
      val fileName = args(args.length - 1)
      val contents = createContent(args, args.length - 2)

      if (">>".equals(operator))
        doEcho(state, contents, fileName, true)
      else if (">".equals(operator))
        doEcho(state, contents, fileName, false)
      else
        state.setMessage(createContent(args, args.length))



    }

  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {

    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if (dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else
        if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
        else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))

    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory // <- This will always be a Directory because we are not supporting absolute/relative path in Echo command

      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)

      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, contents: String, fileName: String, append: Boolean): State = {

    if (fileName.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ fileName, contents, append)

      if (newRoot == state.root)
        state.setMessage(fileName + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.wd.getAllFoldersInPath))
    }

  }

  // topIndex NON-INCLUSIVE!
  def createContent(args: Array[String], topIndex: Int):String = {

    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {

      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }
}
