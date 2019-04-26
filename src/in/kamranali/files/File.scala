package in.kamranali.files

import in.kamranali.filesystem.FileSystemException


class File(override val parentPath: String, override val name: String, val contents: String) extends DirEntry(parentPath, name) {
  override def asDirectory: Directory = throw new FileSystemException("A file can't be converted into a directory")

  override def getType: String = "File"

  override def asFile: File = this
}

object File {

  def empty(parentPath: String, name: String): File = {
    new File(parentPath, name, "")
  }
}
