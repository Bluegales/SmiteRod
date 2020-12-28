package unemployed.SmiteRod;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != smiteItem)
            return;

        ItemMeta itemMeta = itemStack.getItemMeta();

        // Check NBT tag
        NamespacedKey key = new NamespacedKey(plugin, "smiterod");
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if(!container.has(key , PersistentDataType.INTEGER))
            return;

        boolean infinite = container.get(key, PersistentDataType.INTEGER) == 1;

        // Check for permission
        if (infinite) {
            if (!player.hasPermission("smiterod.use.infinite")) {
                player.sendMessage(Config.instance.noPermission);
                return;
            }
        } else {
            if (!player.hasPermission("smiterod.use.single")) {
                player.sendMessage(Config.instance.noPermission);
                return;
            }
        }

        // Check distance
        Block targetblock = player.getTargetBlock(null, Config.instance.maxDistance);
        if (targetblock.isEmpty()){
            player.sendMessage(Config.instance.toFarAway);
            return;
        }
        if (targetblock.getLocation().distance(player.getLocation()) < Config.instance.minDistance)
        {
            player.sendMessage(Config.instance.toClose);
            return;
        }

        world.strikeLightning(targetblock.getLocation());
        if (!Config.instance.useMessage.isEmpty())
            player.sendMessage(Config.instance.useMessage);

        if (!infinite){
            itemStack.setAmount(itemStack.getAmount()-1);
        }
    }
}
