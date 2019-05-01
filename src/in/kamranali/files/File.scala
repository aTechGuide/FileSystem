package in.kamranali.files

import in.kamranali.filesystem.FileSystemException


class File(override val parentPath: String, override val name: String, val contents: String) extends DirEntry(parentPath, name) {

  override def asDirectory: Directory = throw new FileSystemException("A file can't be converted into a directory")
  override def asFile: File = this

  override def getType: String = "File"

  override def isDirectory: Boolean = false
  override def isFile: Boolean = true

  def appendContents(contents: String): File = {
    setContents(contents + "\n" + contents)
  }

  def setContents(contents: String): File = {
    new File(parentPath, name, contents)
  }
}

object File {

  def empty(parentPath: String, name: String): File = {
    new File(parentPath, name, "")
  }
}
