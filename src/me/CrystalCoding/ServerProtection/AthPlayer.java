package me.CrystalCoding.ServerProtection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class AthPlayer implements Listener{

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
	    Player player = event.getPlayer();
	    
	    if (AthProtection.NO_LOGIN && !AthProtection.isAdmin(player.getName()))
	    	if (AthProtection.NEW && !player.hasPlayedBefore())
	    		player.kickPlayer("The admin has disabled logging in at this time.");
	    	else if (!AthProtection.NEW)
	    		player.kickPlayer("The admin has disabled logging in at this time.");
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();

		if (AthProtection.FREEZE_ALL && !p.hasPermission("sp.nofreeze"))
			event.setCancelled(true);
		
		for (String s : AthProtection.frozenPlayers)
			if (s.equalsIgnoreCase(p.getName()))
				event.setCancelled(true);
	}
}
