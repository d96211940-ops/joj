package ru.modernplugins.itemjoiner.item;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import ru.modernplugins.itemjoiner.ItemJoiner;
import ru.modernplugins.itemjoiner.util.LogUtil;

import java.util.*;

public class ItemManager {

    private final Map<String, CustomItem> items = new HashMap<>();

    public void load() {
        items.clear();
        ConfigurationSection sec = ItemJoiner.getInstance().getConfig().getConfigurationSection("items");
        if (sec == null) return;
        for (String key : sec.getKeys(false)) {
            try {
                items.put(key.toLowerCase(), new CustomItem(key.toLowerCase(), sec.getConfigurationSection(key)));
            } catch (Exception e) {
                LogUtil.error("Ошибка загрузки предмета " + key + ": " + e.getMessage());
            }
        }
        LogUtil.info("Загружено предметов: " + items.size());
    }

    public void reload() {
        ItemJoiner.getInstance().reloadConfig();
        load();
    }

    public boolean exists(String id) {
        return items.containsKey(id.toLowerCase());
    }

    public ItemStack build(String id, int amount) {
        CustomItem ci = items.get(id.toLowerCase());
        return ci == null ? null : ci.build(amount);
    }

    public List<String> getIds() {
        return new ArrayList<>(items.keySet());
    }
}