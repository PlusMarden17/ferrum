package io.coconut.ferrum.utils

import com.google.gson.{JsonArray, JsonObject}
import io.coconut.ferrum.Launcher
import io.coconut.ferrum.types.*

object Checker {
  def whichType(version: String): String = {
    val versionType = if (version.startsWith("b") || version.contains("beta")) {
      VersionType.BETA
    } else if (version.startsWith("a") || version.contains("alpha")) {
      VersionType.ALPHA
    } else if (version.contains("w") || version.contains("-pre") || version.contains("-rc")) {
      VersionType.SNAPSHOT
    } else {
      VersionType.RELEASE
    }

    versionType.secondName
  }

  def isByRules(rules: JsonArray): Boolean = {
    val os = System.getProperty("os.name").toLowerCase

    import scala.util.boundary
    import scala.util.boundary.break

    boundary:
      for (i <- 0 until rules.size()) {
        val rule = rules.get(i).getAsJsonObject
        val action = rule.get("action").getAsString

        if (rule.has("os")) {
          val osRule = rule.getAsJsonObject("os")
          val osName = if (osRule.has("name")) osRule.get("name").getAsString else ""

          var matches = false
          if (osName.equals("windows") && os.contains("win")) matches = true
          if (osName.equals("linux") && os.contains("nux")) matches = true
          if (osName.equals("osx") && os.contains("mac")) matches = true

          if (action.equals("allow") && !matches) break(false)
          if (action.equals("disallow") && matches) break(false)
        }
      }

      true
  }
}
