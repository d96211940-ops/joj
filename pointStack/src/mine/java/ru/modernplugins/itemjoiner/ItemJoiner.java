package ru.modernplugins.itemjoiner;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import ru.modernplugins.itemjoiner.command.ItemCommand;
import ru.modernplugins.itemjoiner.item.ItemManager;
import ru.modernplugins.itemjoiner.listener.*;
import ru.modernplugins.itemjoiner.util.LogUtil;

@Getter
public final class ItemJoiner extends JavaPlugin {

    private static ItemJoiner instance;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        LogUtil.init();

        itemManager = new ItemManager();
        itemManager.load();

        getCommand("itemjo").setExecutor(new ItemCommand());

        getServer().getPluginManager().registerEvents(new ArmorListener(), this);
        getServer().getPluginManager().registerEvents(new SnowballListener(), this);
        getServer().getPluginManager().registerEvents(new RepairListener(), this);
        getServer().getPluginManager().registerEvents(new PotionListener(), this);
    }

    @Override
    public void onDisable() {
        LogUtil.close();
    }

    public static ItemJoiner getInstance() {
        return instance;
    }
}