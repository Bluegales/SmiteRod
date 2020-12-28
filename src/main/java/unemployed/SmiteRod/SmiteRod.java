package unemployed.SmiteRod;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public final class SmiteRod extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        //HandleConfig
        this.saveDefaultConfig();
        Config.instance.setup(getDataFolder());
        //Commands
        this.getCommand("smiteRod").setExecutor(new SmiteCommand(this));
        //Listeners
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
