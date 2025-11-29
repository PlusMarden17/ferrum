package io.coconut.ferrum;

import io.coconut.ferrum.get.*;
import io.coconut.ferrum.utils.*;

public class Launcher {

    public static final String MINECRAFT_DIR = System.getProperty("user.home") + "/.minecraft";

    private String username;
    private String min;
    private String max;

    public void setUsername(String username) {
        if (username.isEmpty()) {
            username = "Player";
        }

        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setMem(int minV, int maxV) {
        if (minV == 0 || maxV == 0) {
            this.min = Formater.formatMin(1);
            this.max = Formater.formatMax(2);
        } else {
            this.min = Formater.formatMin(minV);
            this.max = Formater.formatMax(maxV);
        }
    }

    public void setVersion(String version) {

    }

    public static void launch() {

    }
}