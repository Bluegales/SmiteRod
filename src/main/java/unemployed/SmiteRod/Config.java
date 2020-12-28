package unemployed.SmiteRod;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    public static Config instance = new Config();
    public File conf;

    public String noPermission;
    public String item;
    public String itemName;
    public String itemLore;
    public String infiniteLore;
    public int distance;

    public void loadValues() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(this.conf);
        this.noPermission = config.getString("noPermission").replace("&", "§");
        this.item = config.getString("smiteRod.item");
        this.itemName = config.getString("smiteRod.displayname").replace("&", "§");
        this.itemLore = config.getString("smiteRod.lore").replace("&", "§");
        this.infiniteLore = config.getString("smiteRod.infinitelore").replace("&", "§");
        this.distance = config.getInt("smiteRod.distance");
    }

    public void setup(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.conf = new File(dir + File.separator + "config.yml");
        loadValues();
    }

    public FileConfiguration getConfig() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(this.conf);
        return config;
    }

    public void write(File dir, String loc, Object obj) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        this.conf = new File(dir + File.separator + "config.yml");

        getConfig().set(loc, obj);
        try {
            getConfig().save(this.conf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadValues();
    }
}
