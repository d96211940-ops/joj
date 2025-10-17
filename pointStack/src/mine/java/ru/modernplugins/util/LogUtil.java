package ru.modernplugins.itemjoiner.util;

import ru.modernplugins.itemjoiner.ItemJoiner;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    private static Path logFile;

    public static void init() {
        try {
            Path dir = ItemJoiner.getInstance().getDataFolder().toPath().resolve("logs");
            if (!Files.exists(dir)) Files.createDirectories(dir);
            String name = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".log";
            logFile = dir.resolve(name);
            if (!Files.exists(logFile)) Files.createFile(logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String msg) {
        write("[INFO] " + msg);
    }

    public static void error(String msg) {
        write("[ERROR] " + msg);
    }

    private static void write(String line) {
        try {
            Files.writeString(logFile, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + line + System.lineSeparator(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() {}
}