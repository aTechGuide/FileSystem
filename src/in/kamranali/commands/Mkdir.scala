package in.kamranali.commands

import in.kamranali.files.{DirEntry, Directory}
import in.kamranali.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry = {
    Directory.empty(state.wd.path, name)
  }
}
