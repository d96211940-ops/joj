package ru.modernplugins.itemjoiner.util;

import java.util.*;

public class Cooldown {

    private static final Map<UUID, Map<String, Long>> map = new HashMap<>();

    public static boolean isOnCooldown(UUID uuid, String id, int seconds) {
        long now = System.currentTimeMillis();
        return map.computeIfAbsent(uuid, k -> new HashMap<>())
                .getOrDefault(id, 0L) > now - seconds * 1000L;
    }

    public static void setCooldown(UUID uuid, String id) {
        map.get(uuid).put(id, System.currentTimeMillis());
    }
}