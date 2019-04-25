package in.kamranali.commands

import in.kamranali.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State = {
    state.setMessage(state.wd.path)
  }
}
