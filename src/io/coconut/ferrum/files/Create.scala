package io.coconut.ferrum.files

import java.io.IOException
import java.nio.file.{Files, Paths}
import io.coconut.ferrum.Launcher.GAME_PATH

object Create {
  def createDirs(version: String): Unit = {
    try {
      Files.createDirectories(Paths.get(s"$GAME_PATH/versions"))
      Files.createDirectories(Paths.get(s"$GAME_PATH/libraries"))
      Files.createDirectories(Paths.get(s"$GAME_PATH/assets/indexes"))
      Files.createDirectories(Paths.get(s"$GAME_PATH/assets/objects"))
      Files.createDirectories(Paths.get(s"$GAME_PATH/natives"))

      Files.createDirectories(Paths.get(s"$GAME_PATH/versions/$version"))
      Files.createDirectories(Paths.get(s"$GAME_PATH/natives/$version"))
    } catch {
      case e: IOException => {
        System.err.println(s"An error occurred while trying to create directories: ${e.getMessage}")
      }
    }
  }
}
