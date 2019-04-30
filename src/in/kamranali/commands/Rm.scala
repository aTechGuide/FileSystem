package in.kamranali.commands
import in.kamranali.files.Directory
import in.kamranali.filesystem.State

class Rm(name: String) extends Command {

  override def apply(state: State): State = {

    //1. Get Working dir
    val wd = state.wd

    //2. get absolute path
    val absolutePath = {

      if (name.startsWith(Directory.SEPARATOR)) name
      else if (wd.isRoot) wd.path + name
      else wd.path + Directory.SEPARATOR + name
    }

    //3. Do checks
    if (Directory.ROOT_PATH.equals(absolutePath))
      state.setMessage("Not Supported to DELETE Root Path")
    else
    //4. find the entry to remove and update structure like we do for mkdir
      doRm(state, absolutePath)

  }

  def doRm(state: State, path: String): State = {

    def rmHelper(currentDirectory: Directory, path: List[String]): Directory = {

      if (path.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.removeEntry(path.head)
      else {
        val nextDirectory = currentDirectory.findEntry(path.head)
        if (!nextDirectory.isDirectory) currentDirectory
        else {
          val newNextDirectory = rmHelper(nextDirectory.asDirectory, path.tail)
          if (newNextDirectory == nextDirectory) currentDirectory
          else currentDirectory.replaceEntry(path.head, newNextDirectory)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList

    val newRoot: Directory = rmHelper(state.root, tokens)

    if (newRoot == state.root) {
      state.setMessage(path + ": no such file or directory")
    } else {
      State(newRoot, newRoot.findDescendant(state.wd.path.substring(1)))
    }

  }
}
