package in.kamranali.commands

import in.kamranali.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State = state.setMessage("Command Not Found!")
}
