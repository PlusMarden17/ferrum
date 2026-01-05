package io.coconut.ferrum.types

enum VersionType(val secondName: String) {
  case RELEASE extends VersionType("release")
  case SNAPSHOT extends VersionType("snapshot")
  case BETA extends VersionType("old_beta")
  case ALPHA extends VersionType("old_alpha")

  // Fallback for Java
  def getSecondName: String = secondName
}
