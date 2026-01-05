package io.coconut.ferrum

import io.coconut.ferrum.files.{Create, Download}
import io.coconut.ferrum.types.VersionType
import io.coconut.ferrum.utils.{Command, Formatter}

import java.io.File
import java.nio.file.{Files, Paths}
import scala.compiletime.uninitialized

object Launcher {
  val VERSION_LIST: String = "https://launchermeta.mojang.com/mc/game/version_manifest.json"
  val GAME_PATH: String = System.getProperty("user.home") + "/.Fminecraft"
}


class Launcher {
  private var username: String = uninitialized
  private var version: String = uninitialized
  private var min: String = uninitialized
  private var max: String = uninitialized
  private var versionType: VersionType = uninitialized

  def setUsername(username: String): Unit = {
    if (username == null || username.trim.isEmpty) {
      System.err.println("Error: Username cannot be null or empty!")
    }
    this.username = username
  }

  def setVersion(version: String): Unit = {
    this.version = version
  }

  def setMemory(minV: Int, maxV: Int): Unit = {
    if (minV <= 0 || maxV <= 0 || maxV < minV) {
      this.min = Formatter.formatMin(1)
      this.max = Formatter.formatMax(2)
    } else {
      this.min = Formatter.formatMin(minV)
      this.max = Formatter.formatMax(maxV)
    }
  }

  def getUsername: String = username
  def getVersion: String = version
  def getMin: String = min
  def getMax: String = max

  def launch(): Unit = {
    Create.createDirs(this.getVersion)

    val versionURL = Download.findURL(this.version)
    if (versionURL == null) {
      throw new Exception("Version not found!")
    }
    val versionInfo = Download.downloadJSON(versionURL)

    val versionDir = s"${Launcher.GAME_PATH}/versions/$version"
    val jarPath = s"$versionDir/$version.jar"
    val clientURL = versionInfo.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString
    if (!Files.exists(Paths.get(jarPath))) {
      System.out.println("Downloading client jar...")
      Download.downloadFile(clientURL, jarPath)
    }

    var classpath = Download.downloadLibs(versionInfo, this.version)
    classpath += File.pathSeparator + jarPath

    val assetIndexID = versionInfo.getAsJsonObject("assetIndex").get("id").getAsString
    Download.downloadAssets(versionInfo)

    Command.buildCommand(this, classpath, versionInfo, assetIndexID)
  }
}
