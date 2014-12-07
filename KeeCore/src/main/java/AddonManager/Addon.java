package AddonManager;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public interface Addon {

	public void onLoad();
	
	public FileConfiguration getConfig();
	
	public void saveConfig(FileConfiguration cfg);
	
	public void reloadConfig();
	
	public void PrintConsole(String message);
	
	public File getDatafolder();

}
