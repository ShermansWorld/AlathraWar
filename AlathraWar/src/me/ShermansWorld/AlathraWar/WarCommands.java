// 
// Decompiled by Procyon v0.5.36
// 

package me.ShermansWorld.AlathraWar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import org.bukkit.command.CommandExecutor;

public class WarCommands implements CommandExecutor
{
    public static ArrayList<War> wars;
    
    static {
        WarCommands.wars = new ArrayList<War>();
    }
    
    public WarCommands(final Main plugin) {
        plugin.getCommand("war").setExecutor((CommandExecutor)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (args.length == 0) {
            p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war help");
        }
        else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("create")) {
                if (!p.hasPermission("AlathraWar.admin")) {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + Helper.color("&cYou do not have permission to do this"));
                    return false;
                }
                if (args.length == 4) {
                    if (WarCommands.wars.isEmpty()) {
                        final War war = new War(args[1].toLowerCase(), args[2].toLowerCase(), args[3].toLowerCase());
                        WarCommands.wars.add(war);
                        final ArrayList<String> side1Players = new ArrayList<String>();
                        final ArrayList<String> side2Players = new ArrayList<String>();
                        side1Players.add("Notch1");
                        side2Players.add("Notch2");
                        war.setSide1Players(side1Players);
                        war.setSide2Players(side2Players);
                        Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side1", (Object)args[2]);
                        Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side2", (Object)args[3]);
                        Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side1players", (Object)side1Players);
                        Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side2players", (Object)side2Players);
                        Main.data.saveConfig();
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "War created with the name " + args[1].toLowerCase() + ", " + args[2] + " vs. " + args[3]);
                        Main.warLogger.log(p.getName() + " created a new war with the name " + args[1].toLowerCase() + ", " + args[2] + " vs. " + args[3]);
                    }
                    else {
                        for (int i = 0; i < WarCommands.wars.size(); ++i) {
                            if (WarCommands.wars.get(i).getName().equalsIgnoreCase(args[1])) {
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + "A war already exists with that name! To view wars type /war list");
                                break;
                            }
                            ++i;
                            final War war2 = new War(args[1], args[2], args[3]);
                            WarCommands.wars.add(war2);
                            final ArrayList<String> side1Players2 = new ArrayList<String>();
                            final ArrayList<String> side2Players2 = new ArrayList<String>();
                            side1Players2.add("Notch1");
                            side2Players2.add("Notch2");
                            war2.setSide1Players(side1Players2);
                            war2.setSide2Players(side2Players2);
                            Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side1", (Object)args[2]);
                            Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side2", (Object)args[3]);
                            Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side1players", (Object)side1Players2);
                            Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side2players", (Object)side2Players2);
                            Main.data.saveConfig();
                            p.sendMessage(String.valueOf(Helper.Chatlabel()) + "War created with the name " + args[1].toLowerCase() + ", " + args[2] + " vs. " + args[3]);
                            Main.warLogger.log(p.getName() + " created a new war with the name " + args[1].toLowerCase() + ", " + args[2] + " vs. " + args[3]);
                        }
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war create [name] [side1] [side2]");
                }
            }
            else if (args[0].equalsIgnoreCase("delete")) {
                if (!p.hasPermission("AlathraWar.admin")) {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + Helper.color("&cYou do not have permission to do this"));
                    return false;
                }
                if (args.length == 2) {
                    boolean found = false;
                    for (int j = 0; j < WarCommands.wars.size(); ++j) {
                        if (WarCommands.wars.get(j).getName().equalsIgnoreCase(args[1])) {
                            p.sendMessage(String.valueOf(Helper.Chatlabel()) + "The war named " + args[1] + " has been deleted");
                            Main.warLogger.log(p.getName() + " deleted a war named " + args[1]);
                            WarCommands.wars.remove(j);
                            Main.data.getConfig().set("Wars." + args[1].toLowerCase(), (Object)null);
                            Main.data.saveConfig();
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Could not find a war with that name! To view wars type /war list");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war delete [name]");
                }
            }
            else if (args[0].equalsIgnoreCase("list")) {
                if (WarCommands.wars.isEmpty()) {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "There are currently no wars");
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Current Wars:");
                    for (final War war : WarCommands.wars) {
                        p.sendMessage(String.valueOf(war.getName()) + " - " + war.getSide1() + " vs. " + war.getSide2());
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("join")) {
                boolean found = false;
                if (args.length == 3) {
                    for (final War war2 : WarCommands.wars) {
                        if (war2.getName().equalsIgnoreCase(args[1].toLowerCase())) {
                            if (war2.getSide1().equalsIgnoreCase(args[2].toLowerCase())) {
                                if (war2.getSide1Players().contains(p.getName())) {
                                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have already joined this war! Type /war leave [war] to leave the war");
                                    return false;
                                }
                                war2.addPlayerSide1(p.getName());
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have joined the war on the side of " + war2.getSide1());
                                TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(p.getUniqueId());
                            	TabAPI.getInstance().getTablistFormatManager().setSuffix(tabPlayer, Helper.color(new StringBuilder("&c[").append(war2.getSide1()).append("]&r").toString()));
                                Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side1players", (Object)war2.getSide1Players());
                                Main.data.saveConfig();
                                Bukkit.getServer().broadcastMessage(String.valueOf(Helper.Chatlabel()) + p.getName() + " has joined " + war2.getName() + " on the side of " + war2.getSide1() + "!");
                                Bukkit.getLogger().info("[AlathraWar] Player " + p.getName() + " has entered " + war2.getName() + " on the side of " + war2.getSide1());
                                Main.warLogger.log(p.getName() + " has entered " + war2.getName() + " on the side of " + war2.getSide1());
                                found = true;
                            }
                            else {
                                if (!war2.getSide2().equalsIgnoreCase(args[2].toLowerCase())) {
                                    continue;
                                }
                                if (war2.getSide2Players().contains(p.getName())) {
                                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have already joined this war! Type /war leave [war] to leave the war");
                                    return false;
                                }
                                war2.addPlayerSide2(p.getName());
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have joined the war on the side of " + war2.getSide2());
                                TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(p.getUniqueId());
                            	TabAPI.getInstance().getTablistFormatManager().setSuffix(tabPlayer, Helper.color(new StringBuilder("&9[").append(war2.getSide2()).append("]&r").toString()));
                                Main.data.getConfig().set("Wars." + args[1].toLowerCase() + ".side2players", (Object)war2.getSide2Players());
                                Main.data.saveConfig();
                                Bukkit.getServer().broadcastMessage(String.valueOf(Helper.Chatlabel()) + p.getName() + " has joined " + war2.getName() + " on the side of " + war2.getSide2() + "!");
                                Bukkit.getLogger().info("[AlathraWar] Player " + p.getName() + " has entered " + war2.getName() + " on the side of " + war2.getSide2());
                                Main.warLogger.log(p.getName() + " has entered " + war2.getName() + " on the side of " + war2.getSide2());
                                found = true;
                            }
                        }
                    }
                    if (!found) {
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "War not found. /war join [name] [side], type /war list to view current wars");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war join [name] [side]");
                }
            }
            else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length == 2) {
                    boolean found = false;
                    boolean found2 = false;
                    for (final War war3 : WarCommands.wars) {
                        if (war3.getName().equalsIgnoreCase(args[1])) {
                            found = true;
                            if (war3.getSide1Players().contains(p.getName())) {
                                found2 = true;
                                war3.removePlayerSide1(p.getName());
                                Main.data.getConfig().set("Wars." + args[1] + ".side1players", (Object)war3.getSide1Players());
                                Main.data.saveConfig();
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have left the war");
                                TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(p.getUniqueId());
                                TabAPI.getInstance().getTablistFormatManager().resetSuffix(tabPlayer);
                                Bukkit.getServer().broadcastMessage(String.valueOf(Helper.Chatlabel()) + p.getName() + " has left " + war3.getName() + ", they were on the side of " + war3.getSide1());
                                Main.warLogger.log(p.getName() + " has left " + war3.getName() + ", they were on the side of " + war3.getSide1());
                            }
                            if (!war3.getSide2Players().contains(p.getName())) {
                                continue;
                            }
                            found2 = true;
                            war3.removePlayerSide2(p.getName());
                            Main.data.getConfig().set("Wars." + args[1] + ".side2players", (Object)war3.getSide2Players());
                            Main.data.saveConfig();
                            p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You have left the war");
                            TabPlayer tabPlayer = TabAPI.getInstance().getPlayer(p.getUniqueId());
                            TabAPI.getInstance().getTablistFormatManager().resetSuffix(tabPlayer);
                            Bukkit.getServer().broadcastMessage(String.valueOf(Helper.Chatlabel()) + p.getName() + " has left " + war3.getName() + ", they were on the side of " + war3.getSide2());
                            Main.warLogger.log(p.getName() + " has left " + war3.getName() + ", they were on the side of " + war3.getSide2());
                        }
                    }
                    if (!found) {
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "War not found. /war leave [name], type /war list to view current wars");
                        return false;
                    }
                    if (!found2) {
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "You are not part of this war!");
                        return false;
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war leave [name]");
                }
            }
            else if (args[0].equalsIgnoreCase("info")) {
                boolean found = false;
                if (args.length == 2) {
                    for (final War war2 : WarCommands.wars) {
                        if (war2.getSide1Players().contains(args[1])) {
                            if (!found) {
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + args[1] + " is currently in the following wars");
                            }
                            found = true;
                            p.sendMessage(String.valueOf(war2.getName()) + " - " + " Fighting for " + war2.getSide1());
                        }
                        else {
                            if (!war2.getSide2Players().contains(args[1])) {
                                continue;
                            }
                            if (!found) {
                                p.sendMessage(String.valueOf(Helper.Chatlabel()) + args[1] + " is currently in the following wars");
                            }
                            found = true;
                            p.sendMessage(String.valueOf(war2.getName()) + " - " + " Fighting for " + war2.getSide2());
                        }
                    }
                    if (!found) {
                        p.sendMessage(String.valueOf(Helper.Chatlabel()) + "This player is not currently in any wars");
                    }
                }
                else {
                    p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war info [player]");
                }
            } else if (args[0].equalsIgnoreCase("help")) {
            	
				if (p.hasPermission("!AlathraWar.admin")) {
					p.sendMessage(Helper.Chatlabel() + "/war create [name] [side1] [side2]");
					p.sendMessage(Helper.Chatlabel() + "/war delete [name]");
				}
				p.sendMessage(Helper.Chatlabel() + "/war join [name] [side]");
				p.sendMessage(Helper.Chatlabel() + "/war leave [name]");
				p.sendMessage(Helper.Chatlabel() + "/war info [player]");
				p.sendMessage(Helper.Chatlabel() + "/war list");

            
            } else {
                p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war help");
            }
        }
        else {
            p.sendMessage(String.valueOf(Helper.Chatlabel()) + "Invalid Arguments. /war help");
        }
        return false;
    }
}
