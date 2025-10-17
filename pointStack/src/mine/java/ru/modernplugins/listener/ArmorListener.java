package ru.modernplugins.itemjoiner.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.modernplugins.itemjoiner.ItemJoiner;

public class ArmorListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof org.bukkit.entity.Player p)) return;

        for (ItemStack armor : p.getInventory().getArmorContents()) {
            if (armor == null || armor.getType().isAir()) continue;

            ItemMeta meta = armor.getItemMeta();
            if (meta == null) continue;

            // проверяем, что это кастомный предмет
            String id = meta.getPersistentDataContainer()
                             .get(new org.bukkit.NamespacedKey(ItemJoiner.getInstance(), "item_id"),
                                  PersistentDataType.STRING);
            if (id == null) continue;

            // если в конфиге стоит unbreakable – не ломаем
            if (meta.isUnbreakable()) {
                // сбросим урон брони, чтобы durability не уменьшалась
                armor.setItemMeta(meta);
            }
        }
    }
}