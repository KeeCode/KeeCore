package DragonCore.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import AddonManager.DragonPlugin;
import DragonCore.DragonCore;

public class PlayerChatEvent implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		for(DragonPlugin dp : DragonCore.getInstance().plugins){
			dp.onChat(e);
		}
	}

}
