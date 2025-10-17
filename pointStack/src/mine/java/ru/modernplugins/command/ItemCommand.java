package ru.modernplugins.itemjoiner.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.modernplugins.itemjoiner.ItemJoiner;
import ru.modernplugins.itemjoiner.item.ItemManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) return false;
        ItemManager im = ItemJoiner.getInstance().getItemManager();

        switch (args[0].toLowerCase()) {
            case "reload" -> {
                if (!sender.hasPermission("itemjo.admin")) {
                    sender.sendMessage("§cНет прав.");
                    return true;
                }
                im.reload();
                sender.sendMessage("§aКонфигурация перезагружена.");
                return true;
            }
            case "give" -> {
                if (!sender.hasPermission("itemjo.admin")) {
                    sender.sendMessage("§cНет прав.");
                    return true;
                }
                if (args.length < 3) {
                    sender.sendMessage("§c/itemjo give <игрок> <id> [кол-во]");
                    return true;
                }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    sender.sendMessage("§cИгрок не найден.");
                    return true;
                }
                String id = args[2].toLowerCase();
                int amount = args.length >= 4 ? Integer.parseInt(args[3]) : 1;
                if (!im.exists(id)) {
                    sender.sendMessage("§cПредмет не найден в конфиге.");
                    return true;
                }
                target.getInventory().addItem(im.build(id, amount));
                sender.sendMessage("§aВыдано.");
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) return Arrays.asList("reload", "give");
        if (args.length == 2 && args[0].equalsIgnoreCase("give"))
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (args.length == 3 && args[0].equalsIgnoreCase("give"))
            return ItemJoiner.getInstance().getItemManager().getIds();
        return Collections.emptyList();
    }
}