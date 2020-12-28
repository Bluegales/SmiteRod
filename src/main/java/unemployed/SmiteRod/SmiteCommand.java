package unemployed.SmiteRod;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class SmiteCommand implements CommandExecutor {
    private SmiteRod plugin;

    // Pass instance of main class to 'plugin'
    public SmiteCommand(SmiteRod plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        switch (args.length) {
            case 1:
            case 2:
                ItemStack item;

                if (args[0].equals("infinite")  || args[0].equals("i")) {
                    if (!sender.hasPermission("smiterod.give.infinite")) {
                        sender.sendMessage(Config.instance.noPermission);
                        return true;
                    }
                    item = generateItem(true);
                } else {
                    int stackSize;
                    try {
                        stackSize = Integer.parseInt(args[0]);
                    } catch (NumberFormatException nfe) {
                        sendHelp(sender);
                        return true;
                    }
                    if (!sender.hasPermission("smiterod.give.single")) {
                        sender.sendMessage(Config.instance.noPermission);
                        return true;
                    }
                    if (stackSize > 64)
                        stackSize = 64;
                    if (stackSize < 1)
                        stackSize = 1;
                    item = generateItem(false);
                    item.setAmount(stackSize);
                }

                Player target;

                if (args.length == 1) {
                    if (!(sender instanceof Player)){
                        sender.sendMessage("Needs a name when executed from console");
                        return true;
                    }
                    target = (Player) sender;
                } else {
                    target = plugin.getServer().getPlayer(args[1]);
                    if (target == null || !target.isOnline()) {
                        sender.sendMessage("Can't find that player");
                        return true;
                    }
                }

                target.getInventory().addItem(item);
                sender.sendMessage("You gave " + target.getName() + " a SmiteRod");
                return true;

            default:
                sendHelp(sender);
                return true;
        }
    }

    private ItemStack generateItem(boolean infinite) {
        ItemStack smiteItem = new ItemStack(Material.getMaterial(Config.instance.item));
        ItemMeta smiteItemMeta = smiteItem.getItemMeta();

        // Name
        smiteItemMeta.setDisplayName(Config.instance.itemName);
        // Lore
        List<String> lores = new ArrayList<String>();
        lores.add(Config.instance.itemLore);
        if (infinite)
            lores.add(Config.instance.infiniteLore);
        smiteItemMeta.setLore(lores);
        // Glow
        smiteItemMeta.addEnchant(Enchantment.WATER_WORKER, 70, true);
        smiteItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // NBT tag
        NamespacedKey key = new NamespacedKey(plugin, "smiterod");
        int keyValue = (infinite) ? 1 : 0;
        smiteItemMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, keyValue);

        smiteItem.setItemMeta(smiteItemMeta);
        return smiteItem;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("Infinite Rod:");
        sender.sendMessage("/SmiteRod < i | infinite > [Player]");
        sender.sendMessage("Single Rod:");
        sender.sendMessage("/SmiteRod < amount > [Player]");
    }
}
