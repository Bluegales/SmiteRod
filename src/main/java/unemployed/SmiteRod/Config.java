package unemployed.SmiteRod;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    public static Config instance = new Config();
    public File conf;

    public String noPermission;
    public String toFarAway;
    public String toClose;
    public String useMessage;
    public String item;
    public String itemName;
    public String itemLore;
    public String infiniteLore;
    public int maxDistance = 10;
    public int minDistance = 2;

    public void loadValues() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(this.conf);
        this.noPermission = config.getString("noPermission").replace("&", "§");
        this.toFarAway = config.getString("toFarAway").replace("&", "§");
        this.toClose = config.getString("toClose").replace("&", "§");
        this.useMessage = config.getString("useMessage").replace("&", "§");
        this.item = config.getString("smiteRod.item");
        this.itemName = config.getString("smiteRod.displayname").replace("&", "§");
        this.itemLore = config.getString("smiteRod.lore").replace("&", "§");
        this.infiniteLore = config.getString("smiteRod.infiniteLore").replace("&", "§");
        this.minDistance = config.getInt("smiteRod.minDistance");
        this.maxDistance = config.getInt("smiteRod.maxDistance");
    }

    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.conf = new File(dir + File.separator + "config.yml");
        loadValues();
    }
}
