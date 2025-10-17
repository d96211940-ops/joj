package ru.modernplugins.itemjoiner.item;

import com.google.common.base.Enums;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class CustomItem {

    private final String id;
    private Material material;
    private String name;
    private boolean unbreakable;
    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    private List<AttributeConfig> attributes = new ArrayList<>();
    private int charges = -1;
    private int cooldown = 0;
    private List<PotionEffect> effects = new ArrayList<>();
    private RepairConfig repair;

    public CustomItem(String id, ConfigurationSection cfg) {
        this.id = id;
        this.material = Enums.getIfPresent(Material.class, cfg.getString("material", "STONE"))
                .or(Material.STONE);
        this.name = ChatColor.translateAlternateColorCodes('&', cfg.getString("name", id));
        this.unbreakable = cfg.getBoolean("unbreakable", false);
        this.lore = cfg.getStringList("lore").stream()
                .map(s -> ChatColor.translateAlternateColorCodes('&', s))
                .toList();

        if (cfg.contains("enchants")) {
            cfg.getConfigurationSection("enchants").getValues(false).forEach((k, v) -> {
                Enchantment en = Enchantment.getByKey(NamespacedKey.minecraft(k.toLowerCase()));
                if (en != null) enchants.put(en, (Integer) v);
            });
        }

        if (cfg.contains("attributes")) {
            cfg.getMapList("attributes").forEach(map -> {
                String slot = map.get("slot").toString();
                String type = map.get("type").toString();
                double amount = Double.parseDouble(map.get("amount").toString());
                attributes.add(new AttributeConfig(slot, type, amount));
            });
        }

        this.charges = cfg.getInt("charges", -1);
        this.cooldown = cfg.getInt("cooldown", 0);

        if (cfg.contains("effects")) {
            cfg.getMapList("effects").forEach(map -> {
                PotionEffectType pet = PotionEffectType.getByName(map.get("type").toString());
                if (pet != null) {
                    int dur = Integer.parseInt(map.getOrDefault("duration", "1").toString()) * 20;
                    int amp = Integer.parseInt(map.getOrDefault("amplifier", "0").toString());
                    effects.add(new PotionEffect(pet, dur, amp));
                }
            });
        }

        if (cfg.contains("repair")) {
            ConfigurationSection r = cfg.getConfigurationSection("repair");
            int percent = r.getInt("percent");
            List<String> targets = r.getStringList("targets");
            this.repair = new RepairConfig(percent, targets);
        }
    }

    public ItemStack build(int amount) {
        ItemStack stack = new ItemStack(material, amount);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        meta.setUnbreakable(unbreakable);
        if (meta instanceof Damageable d) d.setDamage(0);
        enchants.forEach((e, l) -> meta.addEnchant(e, l, true));

        // attributes
        attributes.forEach(ac -> {
            EquipmentSlot slot = Enums.getIfPresent(EquipmentSlot.class, ac.slot).orNull();
            Attribute attr = Enums.getIfPresent(Attribute.class, ac.type).orNull();
            if (slot != null && attr != null) {
                meta.addAttributeModifier(attr, new AttributeModifier(UUID.randomUUID(),
                        id + "_" + ac.type, ac.amount, AttributeModifier.Operation.ADD_NUMBER, slot));
            }
        });

        // charges
        if (charges > 0) {
            List<String> l = meta.hasLore() ? meta.getLore() : new ArrayList<>();
            l.add("");
            l.add(ChatColor.GRAY + "Зарядов: " + ChatColor.GREEN + charges);
            meta.setLore(l);
        }

        stack.setItemMeta(meta);

        // PersistentDataContainer
        NamespacedKey key = new NamespacedKey(ItemJoiner.getInstance(), "item_id");
        stack.editMeta(m -> m.getPersistentDataContainer().set(key, org.bukkit.persistence.PersistentDataType.STRING, id));
        return stack;
    }

    public String getId() {
        return id;
    }

    public int getCharges() {
        return charges;
    }

    public int getCooldown() {
        return cooldown;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public RepairConfig getRepair() {
        return repair;
    }

    private record AttributeConfig(String slot, String type, double amount) {}
    public record RepairConfig(int percent, List<String> targets) {}
}