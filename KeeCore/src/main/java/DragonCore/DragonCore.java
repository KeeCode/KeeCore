package DragonCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.error.YAMLException;

import AddonManager.AddonLoader;
import AddonManager.DragonPlugin;
import AddonManager.PluginCommandYamlParser;
import AddonManager.PluginDescriptionFile;

public class DragonCore extends JavaPlugin {
	
	public ArrayList<DragonPlugin> plugins;
	public static DragonCore plugin;
	AddonLoader a = new AddonLoader();
	public HashMap<Command, DragonPlugin> commandMap = new HashMap<Command, DragonPlugin>();
	
	public void onEnable(){
		plugin = this;
		System.out.println("[INFO] Loading addons");
		plugins = a.loadAddons();
	}
	
	public void onDisable(){
		for(Command cmd : commandMap.keySet()){
			unregisterCommand(cmd.getName());
		}
		
		for(DragonPlugin dp : plugins){
			for(Listener l : dp.listener){
				HandlerList.unregisterAll(l);
			}
		}
	}
	
	
	public static DragonCore getInstance(){
		return plugin;
	}

	public static SimpleCommandMap getCommandMap() {
		try {
			Field field = SimplePluginManager.class.getDeclaredField("commandMap");
			field.setAccessible(true);
			SimpleCommandMap scm = (SimpleCommandMap) field.get(Bukkit.getPluginManager());
			return scm;
		}catch (Exception e) {
			return null;
		}
	}

	public void registerCommands(Map<String, Map<String, Object>> commands, DragonPlugin dp){
    List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin, commands);

    if (!pluginCommands.isEmpty()) {
        getCommandMap().registerAll(plugin.getDescription().getName(), pluginCommands);
        for(Command cmd : pluginCommands){
        	commandMap.put(cmd, dp);
        }
    }
	}
	
	 public PluginDescriptionFile getPluginDescription(File file) throws InvalidDescriptionException {
	        Validate.notNull(file, "File cannot be null");

	        JarFile jar = null;
	        InputStream stream = null;

	        try {
	            jar = new JarFile(file);
	            JarEntry entry = jar.getJarEntry("addon.dc");

	            if (entry == null) {
	                throw new InvalidDescriptionException(new FileNotFoundException("Jar does not contain addon.dc"));
	            }

	            stream = jar.getInputStream(entry);

	            return new PluginDescriptionFile(stream);

	        } catch (IOException ex) {
	            throw new InvalidDescriptionException(ex);
	        } catch (YAMLException ex) {
	            throw new InvalidDescriptionException(ex);
	        } finally {
	            if (jar != null) {
	                try {
	                    jar.close();
	                } catch (IOException e) {
	                }
	            }
	            if (stream != null) {
	                try {
	                    stream.close();
	                } catch (IOException e) {
	                }
	            }
	        }
	    }
	 
	 public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		 if(cmd.getName().equalsIgnoreCase("ds")){
			 if(args.length == 0){
				 sender.sendMessage(new String[]{
						 "§b[========] §cDragonSuite §b[========]",
						 "§aDragonSuite - Your expandable suite",
						 "§6Developed by DragonSephHD Aka. Peace1333",
						 "§b[========] §cDragonSuite §b[========]",
				 });
				 return true;
			 }
			 if(args[0].equalsIgnoreCase("reload")){
				 if(sender.hasPermission("ds.reload")){	
					 for(Command cmd1 : commandMap.keySet()){
						unregisterCommand(cmd1.getName());
					}
					
					for(DragonPlugin dp : plugins){
						for(Listener l : dp.listener){
							HandlerList.unregisterAll(l);
						}
					}
					plugins.clear();
					commandMap.clear();
					plugins = a.loadAddons();
					 sender.sendMessage("§aReloaded all addons");
				 }
			 }
			 
			 if(args[0].equalsIgnoreCase("addons")){
				 if(sender.hasPermission("ds.addon.control")){
					 if(args[1].equalsIgnoreCase("list")){
						 sender.sendMessage("§cAddons:§a");
						 for(DragonPlugin dp : plugins){
							 sender.sendMessage("§a" + dp.name);
						 }
					 }
				 }
			 }
		 }
		 if(commandMap.containsKey(cmd)){
			 DragonPlugin dp = commandMap.get(cmd);
			 dp.onCommand(sender, cmd, label, args);
		 }
		 return true;
	 }
	 
		public static boolean unregisterCommand(String label) {
			SimpleCommandMap scm = getCommandMap();
			if (scm != null) {
				try {
					Field field = SimpleCommandMap.class.getDeclaredField("knownCommands");
					field.setAccessible(true);
					@SuppressWarnings("unchecked")
					Map<String, Command> cmds = (Map<String, Command>) field.get(scm);
					if (cmds.containsKey(label)) {
						cmds.remove(label);
						return true;
					}else return true;
				}catch (Exception e) {
					return false;
				}
			}else return false;
		}
	 
}
