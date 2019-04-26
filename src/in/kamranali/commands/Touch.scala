package in.kamranali.commands

import in.kamranali.files.{DirEntry, File}
import in.kamranali.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry = {

    File.empty(state.wd.path, name)
  }
}
