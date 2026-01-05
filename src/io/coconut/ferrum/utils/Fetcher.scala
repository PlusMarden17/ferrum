package io.coconut.ferrum.utils

import com.google.gson.{JsonObject, JsonParser}

import java.io.IOException
import scala.io.Source

object Fetcher {
  def fetchJSON(urlStr: String): JsonObject = {
    try {
      val source = Source.fromURL(urlStr)
      val content = try source.mkString finally source.close()
      JsonParser.parseString(content).getAsJsonObject
    } catch {
      case e: IOException => {
        throw new IOException(s"Failed to fetch JSON from $urlStr", e)
      }
    }
  }

  def fetchOS(): String = {
    System.getProperty("os.name").toLowerCase
  }

  def getArch: String = {
    val arch = System.getProperty("os.arch").toLowerCase

    if (arch.contains("aarch64") || arch.contains("arm")) {
      "arm"
    } else if (arch.contains("64")) {
      "x64"
    } else if (arch.contains("i386") || arch.contains("86")) {
      "x86"
    } else {
      System.err.println(s"Error: Unsupported CPU architecture ($arch)")
      null
    }
  }

  def fetchJDK(): String = {
    System.getProperty("java.version")
  }
}
