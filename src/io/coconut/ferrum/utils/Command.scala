package io.coconut.ferrum.utils

import com.google.gson.JsonObject
import io.coconut.ferrum.Launcher

object Command {
  def buildCommand(launcher: Launcher, classpath: String, versionInfo: JsonObject, assetIndexId: String): Unit = {
    try {
      val mainClass = versionInfo.get("mainClass").getAsString
      val nativesDir = s"${Launcher.GAME_PATH}/natives/${launcher.getVersion}"

      val command = Seq(
        "java",
        launcher.getMin,
        launcher.getMax,
        s"-Djava.library.path=$nativesDir",
        "-cp", classpath,
        mainClass,
        "--username", launcher.getUsername,
        "--version", launcher.getVersion,
        "--gameDir", Launcher.GAME_PATH,
        "--assetsDir", s"${Launcher.GAME_PATH}/assets",
        "--assetIndex", assetIndexId,
        "--uuid", "00000000-0000-0000-0000-000000000000",
        "--accessToken", "0",
        "--userType", "legacy",
        "--versionType", "release"
      )

      val pb = new java.lang.ProcessBuilder(command*)
      pb.inheritIO()
      val process = pb.start()
      process.waitFor()
    } catch {
      case e: Exception => {
        System.err.println(s"An error occurred while trying to launch the game: ${e.getMessage}")
      }
    }
  }
}
