package io.coconut.ferrum.files

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Files, Paths}
import java.util.zip.{ZipEntry, ZipInputStream}

object Natives {

  def getOS: String = {
    val os = System.getProperty("os.name").toLowerCase()

    if (os.contains("win")) {
      return "natives-windows"
    } else if (os.contains("nux")) {
      return "natives-linux"
    } else if (os.contains("mac")) {
      return "natives-macos"
    }

    null
  }

  @throws[Exception]
  def extractNatives(jarPath: String, dir: String): Unit = {
    Files.createDirectories(Paths.get(dir))

    val zis = new ZipInputStream(new FileInputStream(jarPath))

    try {
      var entry: ZipEntry = zis.getNextEntry
      val buffer = new Array[Byte](8192)

      while (entry != null) {
        val name = entry.getName

        val isMeta = name.startsWith("META-INF/") ||
          name.contains(".git") ||
          name.contains(".sha1")

        if (!isMeta) {
          val isNative = name.endsWith(".dll") ||
            name.endsWith(".so") ||
            name.endsWith(".dylib") ||
            name.endsWith(".jnilib")

          if (isNative) {
            val output = new File(dir, new File(name).getName)
            val fos = new FileOutputStream(output)

            try {
              var len = zis.read(buffer)
              while (len > 0) {
                fos.write(buffer, 0, len)
                len = zis.read(buffer)
              }
            } finally {
              fos.close()
            }
          }
        }

        zis.closeEntry()
        entry = zis.getNextEntry
      }
    } finally {
      zis.close()
    }
  }
}
