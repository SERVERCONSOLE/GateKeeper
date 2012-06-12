package me.CrystalCoding.ServerProtection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AthPlayer implements Listener{

	@EventHandler (ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();
	    
	    if (AthProtection.NO_LOGIN && !AthProtection.isAdmin(player.getName()))
	    	player.kickPlayer("The admin has disabled logging in at this time.");
	}
}
