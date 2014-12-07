package AddonManager;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Map;

import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;

import DragonCore.DragonCore;

public class AddonLoader {
	File file;

	public ArrayList<DragonPlugin> loadAddons(){
		ArrayList<DragonPlugin> plugins = new ArrayList<DragonPlugin>();

		File pluginDir = new File(DragonCore.getInstance().getDataFolder() + "/addons");
		if(pluginDir.exists() == false)
		pluginDir.mkdirs();

		File[] jars = pluginDir.listFiles(new FileFilter()
		{
			public boolean accept(File pathname)
			{
				return pathname.getName().endsWith(".jar");
			}
		});

		try
		{
			if(jars.length != 0){
			for (File f : jars)
			{
				   final PluginDescriptionFile description;
			        try {
			            description = DragonCore.plugin.getPluginDescription(f);
			        } catch (InvalidDescriptionException ex) {
			            throw new InvalidPluginException(ex);
			        }
			Map<String, Map<String, Object>> commands = description.getCommands();
				
			      final PluginClassLoader loader;
			        try {
			            loader = new PluginClassLoader(getClass().getClassLoader() ,description.getMain() ,f);
			        } catch (Exception ex) {
			            throw ex;			        
			            } 

      				
                	DragonPlugin dp;
				
			     Class<?> jarClass = Class.forName(description.getMain(), true, loader);
			              Class<? extends DragonPlugin> plugin = jarClass.asSubclass(DragonPlugin.class);
			   
			             Constructor<? extends DragonPlugin> constructor = plugin.getConstructor();
			    
			                dp = constructor.newInstance();
			                dp.name = description.getName();
			                DragonCore.plugin.registerCommands(commands, dp);
			                System.out.println(dp.name);
	                	  
	      				dp.onLoad();

	      				plugins.add(dp);

			}
			}else{
				System.out.println("[INFO] No addons found");
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			System.out.println("[INFO] No addons found");
		}

		return plugins;
	}
}
