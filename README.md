<p align="center">
    <img width="150" height="150" alt="image" src="https://github.com/user-attachments/assets/63462e3a-a2bf-409e-adca-025ad6757d3c" />
</p>

# Ferrum

Ferrum - проста бібліотека двигун для створення майнкрафт запускачів.

# Використання

Цей код запустить версію 1.20.1 з нікнеймом Player без логів в консоль:

```java
import ferrum.core.Launcher;
import ferrum.core.Profile;
import ferrum.core.User;

public class Main {
  public static void main(String[] args) throws Exception {
    Launcher launcher = new Launcher();
    launcher.muteLogs(true);

    Profile profile = new Profile();
    profile.setVersion("1.20.1");
    profile.setMemory(1, 2);

    User user = new User();
    user.setUsername("Player");

    launcher.launch(profile, user);
  }
}
```

# Збірка

Спочатку потрібно завантажити репозиторій та увійти в нього:
```bash
git clone https://github.com/PlusMarden17/ferrum.git
cd ferrum
```
Тепер якщо ви на Unix-like операційній системі то:
```bash
./gradlew build
```
А якщо ви на Windows то:
```batch
./gradlew.bat build
```
.jar буде знаходитися в "build/libs"
