<p align="center">
    <img width="150" height="150" alt="image" src="https://github.com/user-attachments/assets/63462e3a-a2bf-409e-adca-025ad6757d3c" />
</p>

# Ferrum

Ferrum - a simple Java library that allows you to create a Minecraft launcher.

# Usage

```java
import io.coconut.ferrum.*;

public class Main {
  public static void main(String[] args) {
    Launcher launcher = new Launcher();
    launcher.setUsername("Player");
    launcher.setVersion("1.12.2");
    launcher.setMemory(1, 2);
    launcher.launch();
  }
}
```

# Building

```bash
git clone https://github.com/PlusMarden17/ferrum.git
cd ferrum
mvn clean package
```
.jar file will be stored in "out/assembly.dest/" directory with a name like this: "out.jar"