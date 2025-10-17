@EventHandler
public void onInteract(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    ItemStack item = e.getItem();
    if (item == null) return;
    NamespacedKey key = new NamespacedKey(ItemJoiner.getInstance(), "item_id");
    String id = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    if (!"repair_tool".equals(id)) return;
    Player p = e.getPlayer();
    if (Cooldown.isOnCooldown(p.getUniqueId(), id, 600)) {
        p.sendActionBar(ChatColor.RED + "Кулдаун 10 мин");
        return;
    }
    PlayerInventory inv = p.getInventory();
    for (ItemStack armor : inv.getArmorContents()) {
        if (armor != null && armor.getItemMeta() instanceof Damageable d && d.hasDamage()) {
            int max = armor.getType().getMaxDurability();
            int newDamage = Math.max(0, d.getDamage() - max / 2);
            d.setDamage(newDamage);
            armor.setItemMeta((ItemMeta) d);
        }
    }
    Cooldown.setCooldown(p.getUniqueId(), id);
    p.sendActionBar(ChatColor.GREEN + "Броня отремонтирована на 50 %");
}