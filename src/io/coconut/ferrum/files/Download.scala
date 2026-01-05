package io.coconut.ferrum.files

import com.google.gson.{JsonArray, JsonElement, JsonObject, JsonParser}
import io.coconut.ferrum.Launcher
import io.coconut.ferrum.utils.{Checker, Fetcher}
import java.io.{File, FileOutputStream}
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._

object Download {

  def findURL(version: String): String = {
    try {
      // Assumes Fetcher.fetchJSON now returns a com.google.gson.JsonObject
      val manifest: JsonObject = Fetcher.fetchJSON(Launcher.VERSION_LIST)
      val versions: JsonArray = manifest.getAsJsonArray("versions")

      // Iterate using Java Iterator converted to Scala or standard while loop
      val iterator = versions.iterator()
      while (iterator.hasNext) {
        val v = iterator.next().getAsJsonObject
        if (v.get("id").getAsString == version) {
          return v.get("url").getAsString
        }
      }
      null
    } catch {
      case e: Exception =>
        System.err.println("An error occurred while trying to find URL: " + e.getMessage)
        null
    }
  }

  @throws[Exception]
  def downloadJSON(urlV: String): JsonObject = {
    Fetcher.fetchJSON(urlV)
  }

  @throws[Exception]
  def downloadFile(urlV: String, dest: String): Unit = {
    val file = new File(dest)
    if (!file.getParentFile.exists()) {
      file.getParentFile.mkdirs()
    }

    val url = new URL(urlV)
    val rbc = Channels.newChannel(url.openStream())
    val fos = new FileOutputStream(dest)

    try {
      fos.getChannel.transferFrom(rbc, 0, Long.MaxValue)
    } finally {
      if (fos != null) fos.close()
      if (rbc != null) rbc.close()
    }
  }

  @throws[Exception]
  def downloadLibs(vInfo: JsonObject, version: String): String = {
    val libraries = vInfo.getAsJsonArray("libraries")
    val classpath = new StringBuilder()
    val nativesDir = Launcher.GAME_PATH + "/natives/" + version

    val iterator = libraries.iterator()
    while (iterator.hasNext) {
      val lib = iterator.next().getAsJsonObject

      // Check Rules
      var allowed = true
      if (lib.has("rules")) {
        // Assumes Checker.isByRules accepts com.google.gson.JsonArray
        if (!Checker.isByRules(lib.getAsJsonArray("rules"))) {
          allowed = false
        }
      }

      if (allowed && lib.has("downloads")) {
        val downloads = lib.getAsJsonObject("downloads")

        // Handle Artifacts (Standard Libraries)
        if (downloads.has("artifact")) {
          val artifact = downloads.getAsJsonObject("artifact")
          val libPath = Launcher.GAME_PATH + "/libraries/" + artifact.get("path").getAsString

          if (!Files.exists(Paths.get(libPath))) {
            downloadFile(artifact.get("url").getAsString, libPath)
          }
          classpath.append(libPath).append(File.pathSeparator)
        }

        // Handle Classifiers (Natives)
        if (downloads.has("classifiers")) {
          val classifiers = downloads.getAsJsonObject("classifiers")
          val nativeKey = Natives.getOS // e.g., "natives-windows"

          if (nativeKey != null && classifiers.has(nativeKey)) {
            val nativeLib = classifiers.getAsJsonObject(nativeKey)
            val nativePath = Launcher.GAME_PATH + "/libraries/" + nativeLib.get("path").getAsString

            if (!Files.exists(Paths.get(nativePath))) {
              downloadFile(nativeLib.get("url").getAsString, nativePath)
            }

            Natives.extractNatives(nativePath, nativesDir)
          }
        }
      }
    }
    classpath.toString()
  }

  @throws[Exception]
  def downloadAssets(vInfo: JsonObject): Unit = {
    if (!vInfo.has("assetIndex")) {
      println("Using legacy version")
      return
    }

    val assetIndex = vInfo.getAsJsonObject("assetIndex")
    val indexId = assetIndex.get("id").getAsString
    val indexUrl = assetIndex.get("url").getAsString
    val indexPath = Launcher.GAME_PATH + "/assets/indexes/" + indexId + ".json"

    if (!Files.exists(Paths.get(indexPath))) {
      downloadFile(indexUrl, indexPath)
    }

    // Read file and parse with Gson
    val fileContent = new String(Files.readAllBytes(Paths.get(indexPath)))
    val indexContent = JsonParser.parseString(fileContent).getAsJsonObject
    val objects = indexContent.getAsJsonObject("objects")

    // Iterate over the EntrySet (Keys and Values)
    for (entry <- objects.entrySet().asScala) {
      val hash = entry.getValue.getAsJsonObject.get("hash").getAsString
      val prefix = hash.substring(0, 2)
      val path = Launcher.GAME_PATH + "/assets/objects/" + prefix + "/" + hash

      if (!Files.exists(Paths.get(path))) {
        val url = "https://resources.download.minecraft.net/" + prefix + "/" + hash
        try {
          downloadFile(url, path)
        } catch {
          case e: Exception =>
            System.err.println("Found and skipped invalid asset...")
        }
      }
    }
  }
}
