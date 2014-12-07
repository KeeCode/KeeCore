package AddonManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import DragonCore.DragonCore;

public abstract class DragonPlugin implements Addon  {
	public String name;
	public ArrayList<Listener> listener = new ArrayList<Listener>();

	public void onLoad() {}

	public void saveConfig(FileConfiguration cfg) {
		File f = new File(DragonCore.plugin.getDataFolder() + "/addons/" + name + "/Config.dc");
		try {
			cfg.save(f);
		} catch (IOException e) {
		}
	}

	public void reloadConfig() {}

	public FileConfiguration getConfig() {
		File f = new File(DragonCore.plugin.getDataFolder() + "/addons/" + name + "/Config.dc");
		if(f.exists() == false){
			try {
				f.createNewFile();
			} catch (IOException e) {
			}
		}
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
		return cfg;
		
	}

	public void PrintConsole(String message) 
	{
		System.out.println("[PLUGIN INFO] " + message);
	}

	public void onChat(AsyncPlayerChatEvent e) {}

	public void registerEvent(Listener l) {
		Bukkit.getPluginManager().registerEvents(l, DragonCore.plugin);
		listener.add(l);
	}
	
	public File getDatafolder(){
		File f = new File(DragonCore.plugin.getDataFolder() + "/addons/" + name + "/");		
		if(f.exists() == false){
			f.mkdirs();
		}
		return f;
	}
	
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
    
}
