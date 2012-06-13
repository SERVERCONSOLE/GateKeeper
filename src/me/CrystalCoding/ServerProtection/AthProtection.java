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
	public static PluginDescriptionFile pdfFile;
	/*** Variables ***/
	public static boolean NO_LOGIN = false, NEW = true, FREEZE_ALL = false;
	public static ArrayList<String> frozenPlayers = new ArrayList<String>();
	
	public void onDisable() {
	}

	public void onEnable() {
		s = getServer();
		s.getPluginManager().registerEvents(new AthPlayer(), this);
		pdfFile = getDescription();
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
			if (comm.equalsIgnoreCase("lockdown")) {
				if (myArgs.length >= 1) {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					} else {
						NO_LOGIN = true;
						if (myArgs[0].equalsIgnoreCase("new"))
							NEW = true;
						else if (myArgs[0].equalsIgnoreCase("old"))
							NEW = false;
						else {
							p.sendMessage(ChatColor.RED + "Please type /stoplogin <new/old> - new for new players / old for old players and new");
							return true;
						}
						
						p.sendMessage(ChatColor.GREEN + "The ability to login has been disabled for " + myArgs[0] + " players.");
						return true;
					}
				} else {
					p.sendMessage(ChatColor.RED + "Please type /stoplogin <new/old> - new for new players / old for old players and new");
				}
			} else if (comm.equalsIgnoreCase("stoplockdown")) {
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
			} else if (comm.equalsIgnoreCase("freezeall")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					
					FREEZE_ALL = true;
					s.broadcastMessage(ChatColor.GOLD + "The server admin has frozen everyone.");
					p.sendMessage(ChatColor.GREEN + "You have frozen everyone.");
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("unfreezeall")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					
					FREEZE_ALL = false;
					frozenPlayers.clear();
					s.broadcastMessage(ChatColor.GOLD + "The server admin has unfrozen everyone.");
					p.sendMessage(ChatColor.GREEN + "You have unfrozen everyone.");
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("freeze")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					if (myArgs.length >= 1) {
						for (Player ply : s.getOnlinePlayers()) {
							if (ply.getName().equalsIgnoreCase(myArgs[0])) {
								frozenPlayers.add(ply.getName());
								ply.sendMessage(ChatColor.GOLD + "You have been frozen by the server admin.");
								p.sendMessage(ChatColor.GREEN + "You have frozen " + myArgs[0] + ".");
								break;
							}
						}
					} else {
						p.sendMessage(ChatColor.RED + "Please use /freeze <name>");
					}
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("unfreeze")) {
				try {
					if (!isAdmin(p.getName())) {
						p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
						return true;
					}
					if (myArgs.length >= 1) {
						for (Player ply : s.getOnlinePlayers()) {
							if (ply.getName().equalsIgnoreCase(myArgs[0])) {
								frozenPlayers.remove(ply.getName());
								ply.sendMessage(ChatColor.GOLD + "You have been unfrozen by the server admin.");
								p.sendMessage(ChatColor.GREEN + "You have unfrozen " + myArgs[0] + ".");
								return true;
							}
						}
						p.sendMessage(ChatColor.RED + "That player wasn't found or isn't frozen.");
					} else {
						p.sendMessage(ChatColor.RED + "Please use /freeze <name>");
					}
					return true;
				} catch (Exception e) {
					p.sendMessage(ChatColor.RED + "Invalid input!");
					return false;
				}
			} else if (comm.equalsIgnoreCase("stoplockdown")) {
				if (!isAdmin(p.getName())) {
					p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
					return true;
				} else {
					NO_LOGIN = false;
					p.sendMessage(ChatColor.GREEN + "The ability to login has been enabled.");
					return true;
				}
			} else if (comm.equalsIgnoreCase("gatekeeper")) {
				if (!isAdmin(p.getName())) {
					p.sendMessage(ChatColor.RED + "You dont have permission to use this command.");
					return true;
				}
				p.sendMessage(ChatColor.AQUA + "--------[ " + pdfFile.getName() + " ]--------");
				p.sendMessage(ChatColor.AQUA + "---------[ Made By Baseball435 ]---------");
				p.sendMessage(ChatColor.GOLD + "/lockdown <new/old> - Doesnt let new/old, and new, members log in");
				p.sendMessage(ChatColor.GOLD + "/stoplockdown - Stops the lockdown");
				p.sendMessage(ChatColor.GOLD + "/muteall - Mutes everyone without the sp.nomute node");
				p.sendMessage(ChatColor.GOLD + "/unmuteall - Unmutes everyone");
				p.sendMessage(ChatColor.GOLD + "/kickall - Kicks everyone without the sp.nomute node");
				p.sendMessage(ChatColor.GOLD + "/freezeall - Freezes everyone without the sp.nofreeze node");
				p.sendMessage(ChatColor.GOLD + "/unfreezeall - Unfreezes everyone");
				p.sendMessage(ChatColor.GOLD + "/freeze <name> - Freeze a person by their name");
				p.sendMessage(ChatColor.GOLD + "/unfreeze <name> - Unfreezes a person by their name");
				return true;
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
