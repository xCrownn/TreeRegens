package me.xcrownn.treeregen.Commands;

import de.leonhard.storage.Json;
import me.xcrownn.treeregen.Direction;
import me.xcrownn.treeregen.TreeRegen;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetTreeCommand implements CommandExecutor {

    private final TreeRegen plugin;

    public SetTreeCommand(TreeRegen plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
            if (label.equalsIgnoreCase("settree")) {
                Json mainFile = new Json("Locations", "plugins/TreeRegen");
                mainFile.getOrSetDefault("DO_NOT_EDIT", 0);

                if (args.length == 1) {
                    int nextID = mainFile.getInt("DO_NOT_EDIT");
                    nextID++;
                    mainFile.set("DO_NOT_EDIT", nextID);

                    switch (Direction.getDirection(player.getLocation())) {
                        case NORTH:
                            mainFile.getOrSetDefault(nextID + ".Direction", "NORTH");
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.X", player.getLocation().getBlockX());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Y", player.getLocation().getBlockY());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Z", player.getLocation().getBlockZ() - 1);
                            break;
                        case SOUTH:
                            mainFile.getOrSetDefault(nextID + ".Direction", "SOUTH");
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.X", player.getLocation().getBlockX());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Y", player.getLocation().getBlockY());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Z", player.getLocation().getBlockZ() + 1);
                            break;
                        case EAST:
                            mainFile.getOrSetDefault(nextID + ".Direction", "EAST");
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.X", player.getLocation().getBlockX() + 1);
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Y", player.getLocation().getBlockY());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Z", player.getLocation().getBlockZ());
                            break;
                        case WEST:
                            mainFile.getOrSetDefault(nextID + ".Direction", "WEST");
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.X", player.getLocation().getBlockX() - 1);
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Y", player.getLocation().getBlockY());
                            mainFile.getOrSetDefault(nextID + ".Tree_Location.Z", player.getLocation().getBlockZ());
                            break;
                    }

                    mainFile.getOrSetDefault(nextID + ".Schematic_Name", args[0]);
                    mainFile.getOrSetDefault(nextID + ".TimerStarted", false);

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aLocation set!"));
                } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &cYou must enter the name of the &4schematic &cyou wish to use. &7(ex. large_tree)"));
            }

        return true;
    }


}
//Json mainFile = new Json("Locations", "plugins/TreeRegen");