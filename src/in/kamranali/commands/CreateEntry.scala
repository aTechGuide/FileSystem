package in.kamranali.commands

import in.kamranali.files.{DirEntry, Directory}
import in.kamranali.filesystem.State

abstract class CreateEntry(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains('.')
  }

  override def apply(state: State): State = {

    val wd = state.wd

    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      // mkdir dir1/dir2 is forbidden

      state.setMessage(name + " must not contains separators")
    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name!")
    } else {
      doCreateEntry(state, name)
    }
  }

  def doCreateEntry(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      /*
      Case 1
      someDirectory
        /a
        /b
        (new) /d

        => new someDir
           /a
           /b
           /d

        Case 2
        /a/b
          /c
          /d
          (new) /e

        =>
        new /a  new /b
                    /c
                    /d
                    /e

       */

      if (path.isEmpty) currentDirectory.addEntry(newEntry)
      else {
        /*

        /a/b
          /c
          (new Entry) /e

        currentDirectory /a
        path = ["b"]
         */

        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }


    }

    val wd = state.wd

    //1. Get hold of all the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    //2. create new Directory entry in the wd

    val newEntry: DirEntry = createSpecificEntry(state)
    //val newDir = Directory.empty(wd.path,name)

    //3. Update the whole directory structure starting from root (The directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    //4. find new working directory instance given wd's full path, in the New Directory structure.
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)

  }

  def createSpecificEntry(state: State) : DirEntry

}
