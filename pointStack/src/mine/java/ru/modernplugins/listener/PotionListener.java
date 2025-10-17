@EventHandler
public void onSplash(PlayerInteractEvent e) {
    if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    ItemStack item = e.getItem();
    if (item == null || item.getType() != Material.SPLASH_POTION) return;
    NamespacedKey key = new NamespacedKey(ItemJoiner.getInstance(), "item_id");
    String id = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
    if (id == null) return;
    CustomItem ci = ItemJoiner.getInstance().getItemManager().getItems().get(id);
    if (ci == null) return;
    Player p = e.getPlayer();
    int left = getCharges(item);
    if (left <= 0) {
        p.sendActionBar(ChatColor.RED + "Заряды закончились");
        return;
    }
    left--;
    setCharges(item, left);
    if (left == 0) {
        item.setAmount(0);
    }
    ci.getEffects().forEach(ef -> p.addPotionEffect(ef));
}

private int getCharges(ItemStack item) {
    if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) return 0;
    for (String l : item.getItemMeta().getLore()) {
        if (l.contains("Зарядов:")) {
            return Integer.parseInt(l.split(":")[1].trim());
        }
    }
    return 0;
}

private void setCharges(ItemStack item, int charges) {
    ItemMeta m = item.getItemMeta();
    List<String> lore = m.getLore();
    for (int i = 0; i < lore.size(); i++) {
        if (lore.get(i).contains("Зарядов:")) {
            lore.set(i, ChatColor.GRAY + "Зарядов: " + ChatColor.GREEN + charges);
            break;
        }
    }
    m.setLore(lore);
    item.setItemMeta(m);
}