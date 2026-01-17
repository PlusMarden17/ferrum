<p align="center">
    <img width="150" height="150" alt="image" src="https://github.com/user-attachments/assets/63462e3a-a2bf-409e-adca-025ad6757d3c" />
</p>

# Ferrum

Ferrum - a simple Java library that allows you to create a Minecraft launcher.

# Usage

```java
import io.coconut.ferrum.*;

public class Main {
  public static void main(String[] args) throws Exception {
    /*
    Arguments:
    1: Username
    2: Version
    3: Min RAM
    4: Max RAM
    */
    Launcher launcher = new Launcher("Player", "1.12.2", 1, 2);
    launcher.launch(); // Method to launch the game
  }
}
```

# Building

```bash
git clone https://github.com/PlusMarden17/ferrum.git
cd ferrum
./gradlew build
```
.jar file will be stored in "build/libs"
