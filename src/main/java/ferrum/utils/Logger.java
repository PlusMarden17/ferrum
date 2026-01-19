package ferrum.utils;

import ferrum.core.Launcher;

public class Logger {
    /*
    Цей логер не буде друкувати текст якщо в конфігурація лаунчеру логи вимкнені
    */
   
    public static void print(String text) {
        if (Launcher.defaultLogType == Log.VERBOSE) {
            System.out.println(text);
        }
    }

    public static void error(String text) {
        if (Launcher.defaultLogType == Log.VERBOSE) {
            System.err.println(text);
        }
    }
}