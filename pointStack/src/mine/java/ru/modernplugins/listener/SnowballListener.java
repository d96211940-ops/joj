@EventHandler
public void onHit(ProjectileHitEvent e) {
    if (e.getEntity() instanceof Snowball sb && sb.getShooter() instanceof Player p) {
        ItemStack item = p.getInventory().getItemInMainHand();
        NamespacedKey key = new NamespacedKey(ItemJoiner.getInstance(), "item_id");
        String id = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if ("slowdown_snowball".equals(id)) {
            if (e.getHitEntity() instanceof LivingEntity le) {
                if (Cooldown.isOnCooldown(p.getUniqueId(), id, 15)) {
                    p.sendActionBar(ChatColor.RED + "Кулдаун 15 сек");
                    return;
                }
                le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 3 * 20, 9));
                Cooldown.setCooldown(p.getUniqueId(), id);
            }
        }
    }
}