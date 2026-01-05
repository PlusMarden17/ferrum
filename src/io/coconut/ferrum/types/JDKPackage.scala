package io.coconut.ferrum.types

enum JDKPackage(val os: String, val arch: String, val url: String) {
  case WIN_X64 extends JDKPackage(
    "win",
    "x64",
    "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u392b08.msi"
  )
  case WIN_X86 extends JDKPackage(
    "win",
    "x86",
    "https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u392-b08/OpenJDK8U-jdk_x86-32_windows_hotspot_8u392b08.msi"
  )

  // Placeholders added for the missing Java values
  case LIN_X64 extends JDKPackage("nux", "x64", "")
  case LIN_X86 extends JDKPackage("nux", "x86", "")
  case LIN_ARM extends JDKPackage("nux", "arm", "")

  case MAC_X64 extends JDKPackage("mac", "x64", "")
  case MAC_ARM extends JDKPackage("mac", "arm", "")
}
