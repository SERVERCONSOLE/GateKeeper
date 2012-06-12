package me.CrystalCoding.ServerProtection;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

public class AthProtection extends JavaPlugin{

	public static Server s;
	public static final Logger log = Logger.getLogger("Minecraft");
	
	/*** Variables ***/
	public static boolean NO_LOGIN = false;

	public void onDisable() {
	}

	public void onEnable() {
		s = getServer();
		s.getPluginManager().registerEvents(new AthPlayer(), this);
		PluginDescriptionFile pdfFile = getDescription();
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String comm = command.getName();
		String[] myArgs = args;
		String allArgs = "";
		
		for (int i = 0; i < myArgs.length; i++)
			allArgs += myArgs[i] + " ";

		if (myArgs == null)
			myArgs = new String[0];
		
		if ((sender instanceof Player)) {
			Player p = (Player) sender;
			if (comm.equalsIgnoreCase("stoplogin")) {
				if (!isAdmin(p.getName())) {
					p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
					return true;
				} else {
					NO_LOGIN = true;
					p.sendMessage(ChatColor.GREEN + "The ability to login has been disabled.");
					return true;
				}
			} else if (comm.equalsIgnoreCase("allowlogin")) {
				if (!isAdmin(p.getName())) {
					p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
					return true;
				} else {
					NO_LOGIN = false;
					p.sendMessage(ChatColor.GREEN + "The ability to login has been enabled.");
					return true;
				}
			} else if (comm.equalsIgnoreCase("kickall")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					
					for (Player ply : s.getOnlinePlayers())
						if (!ply.hasPermission("sp.nokick"))
							ply.kickPlayer(allArgs.equalsIgnoreCase("") ? "You were kicked." : allArgs);
					
					p.sendMessage(ChatColor.GREEN + "You have kicked everyone without the \"sp.nokick\" node in permissions.");
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("muteall")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					
					for (Player ply : s.getOnlinePlayers()) {
						if (!ply.hasPermission("sp.nomute")) {
							Essentials e = (Essentials) s.getPluginManager().getPlugin("Essentials");
							e.getUser(ply.getName()).setMuted(true);
							ply.sendMessage(ChatColor.RED + (allArgs.equalsIgnoreCase("") ? "You were muted." : "You were Muted: " + allArgs));
						}
					}
							
					p.sendMessage(ChatColor.GREEN + "You have muted everyone without the \"sp.nomute\" node in permissions.");
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("unmuteall")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					
					for (Player ply : s.getOnlinePlayers()) {
						Essentials e = (Essentials) s.getPluginManager().getPlugin("Essentials");
						if (e.getUser(ply.getName()).isMuted()) {
							e.getUser(ply.getName()).setMuted(false);
							ply.sendMessage(ChatColor.RED + "You Were Unmuted.");
						}
					}
							
					p.sendMessage(ChatColor.GREEN + "You have unmuted everyone.");
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			}
 		}
		return false;
	}
	
	public static boolean isAdmin(String name) {
			Player p = s.getPlayer(name).getPlayer();
			if (p.hasPermission("sp.admin")) {
				return true;
			}
		return false;
	}
}
