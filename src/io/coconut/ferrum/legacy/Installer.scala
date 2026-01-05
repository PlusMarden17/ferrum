package io.coconut.ferrum.legacy

import io.coconut.ferrum.utils.Fetcher

import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

object Installer {
  /*
  Щоб грати в старі Beta/Alpha версії потрібні старі версії Java (По типу Java 8)
  для того щоб користувачеві не потрібно було б встановлювати її самому, якщо версія стара то Java 8
  встановиться сама
  */
  def start(): Unit = {
    val os = Fetcher.fetchOS()
    val arch = Fetcher.getArch

    val urlString = (arch, os) match {
      case (a, o) if a.contains("x64") && o.contains("win") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u392b08.msi"
      case (a, o) if a.contains("x64") && o.contains("nux") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_linux_hotspot_8u392b08.tar.gz"
      case (a, o) if a.contains("x64") && o.contains("mac") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_mac_hotspot_8u392b08.pkg"
      case (a, o) if a.contains("x86") && o.contains("win") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_windows_hotspot_8u392b08.msi"
      case (a, o) if a.contains("x86") && o.contains("nux") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_linux_hotspot_8u392b08.tar.gz"
      case (a, o) if a.contains("arm") && o.contains("nux") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_aarch64_linux_hotspot_8u392b08.tar.gz"
      case (a, o) if a.contains("arm") && o.contains("mac") =>
        "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_aarch64_mac_hotspot_8u392b08.pkg"
      case _ => null
    }

    if (urlString != null) {
      val fileName = urlString.substring(urlString.lastIndexOf('/') + 1)
      download(urlString, fileName)
      install(fileName, os)
    } else {
      System.err.println("Error: Couldn't find download link for your OS!")
    }
  }

  private def download(urlString: String, fileName: String): Unit = {
    try {
      val rbc = Channels.newChannel(new URL(urlString).openStream())
      val fos = new FileOutputStream(fileName)
      try {
        fos.getChannel.transferFrom(rbc, 0, Long.MaxValue)
        System.out.println("Successfully downloaded Java 8!")
      } finally {
        fos.close()
        rbc.close()
      }
    } catch {
      case ie: Exception =>
        System.err.println(s"Something went wrong while trying to download Java 8: ${ie.getMessage}")
    }
  }

  private def install(fileName: String, os: String): Unit = {
    try {
      if (os.contains("win")) {
        val pb = new java.lang.ProcessBuilder("msiexec", "/i", fileName, "/quiet")
        pb.inheritIO().start().waitFor()
      } else if (os.contains("nux")) {
        val mkdir = new java.lang.ProcessBuilder("mkdir", "-p", "java8_runtime")
        mkdir.start().waitFor()
        val tar = new java.lang.ProcessBuilder("tar", "-xzf", fileName, "-C", "java8_runtime", "--strip-components=1")
        tar.inheritIO().start().waitFor()
      } else if (os.contains("mac")) {
        val pb = new java.lang.ProcessBuilder("open", fileName)
        pb.start().waitFor()
      }
    } catch {
      case e: Exception =>
        System.err.println(s"An error occurred while trying to install Java 8: ${e.getMessage}")
    }
  }

  def getJavaPath: String = {
    val os = Fetcher.fetchOS()
    var path = "java"
    val curr = Runtime.version()
    val major = curr.feature()

    if (major == 8) {
      path = if (os.contains("win")) {
        "C:\\Program Files\\Eclipse Adoptium\\jdk-8.0.392.8-hotspot\\bin\\java.exe"
      } else if (os.contains("nux")) {
        ""
      } else if (os.contains("mac")) {
        ""
      } else {
        path
      }
    } else {
      System.out.println("You don't have Java 8 installed on your machine")
    }

    path
  }
}
