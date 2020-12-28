package unemployed.SmiteRod;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlayerListener implements Listener {
    private final SmiteRod plugin;

    // Instance of BukkitPlugin(Main)
    public PlayerListener(SmiteRod plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        World world = player.getWorld();
        // Gets item from Config.
        Material smiteItem = Material.getMaterial(Config.instance.item);

        if (e.getAction() != Action.RIGHT_CLICK_AIR)
            return;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != smiteItem)
            return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasDisplayName())
            return;
        if (!itemMeta.getDisplayName().equalsIgnoreCase(Config.instance.itemName))
            return;
        if (!itemMeta.hasLore())
            return;
        List<String> lore = itemMeta.getLore();
        if (!lore.contains(Config.instance.itemLore))
            return;
        if (!player.hasPermission("smite.use") && !player.isOp()) {
            player.sendMessage(Config.instance.noPermission);
            return;
        }
        Block targetblock = player.getTargetBlock(null, 50);
        if (targetblock.isEmpty()){
            player.sendMessage(ChatColor.GRAY + "To far away");
            return;
        }
        Location smiteLocation = targetblock.getLocation();
        if (player.getLocation().distance(smiteLocation) <= 5) {
            player.sendMessage(ChatColor.GRAY + "Are you trying to kill yourself?");
            return;
        }
        world.strikeLightning(smiteLocation);
        player.sendMessage(ChatColor.GRAY + "By the power of Zeus!");

        if (!lore.contains(Config.instance.infiniteLore)){
            itemStack.setAmount(itemStack.getAmount()-1);
            //player.getInventory().removeItem(itemStack);
        }
    }
}
