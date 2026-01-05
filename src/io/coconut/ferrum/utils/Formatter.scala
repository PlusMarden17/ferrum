package io.coconut.ferrum.utils

object Formatter {
  def formatMin(min: Int): String = {
    s"-Xms${min}G"
  }

  def formatMax(max: Int): String = {
    s"-Xmx${max}G"
  }
}
