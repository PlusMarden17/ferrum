package io.coconut.ferrum;

public class Main {
  public static void main(String[] args) {
    Launcher launcher = new Launcher();
    launcher.setUsername("Player");
    launcher.setVersion("1.12.2");
    launcher.setMemory(1, 2);
    launcher.launch();
  }
}