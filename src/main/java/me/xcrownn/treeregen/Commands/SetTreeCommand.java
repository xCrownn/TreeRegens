package me.xcrownn.treeregen.Commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonhard.storage.Json;
import me.xcrownn.treeregen.Direction;
import me.xcrownn.treeregen.TreeRegen;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.UUID;


public class SetTreeCommand implements CommandExecutor {

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

                    int x = player.getLocation().getBlockX();
                    int y = player.getLocation().getBlockY();
                    int z = player.getLocation().getBlockZ();
                    RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                    RegionManager rm = container.get(BukkitAdapter.adapt(player.getWorld()));

                    switch (Direction.getDirection(player.getLocation())) {
                        case NORTH:
                            ArrayList<String> listFromFile = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");
                            String genUUID = UUID.randomUUID().toString();
                            listFromFile.add(genUUID);
                            mainFile.getOrSetDefault("UUIDTreeConstants", listFromFile);
                            mainFile.getOrSetDefault(genUUID + ".Schematic_Name", args[0]);
                            mainFile.getOrSetDefault(genUUID + ".TimerStarted", false);

                            mainFile.getOrSetDefault(genUUID + ".Direction", "NORTH");
                            mainFile.getOrSetDefault(genUUID + ".Tree_Location.X", x);
                            mainFile.getOrSetDefault(genUUID + ".Tree_Location.Y", y);
                            mainFile.getOrSetDefault(genUUID + ".Tree_Location.Z", z - 1);
                            mainFile.getOrSetDefault(genUUID + ".RegionName", genUUID);

                            ProtectedRegion region1 = new ProtectedCuboidRegion(genUUID, BlockVector3.at(x - 1, y, z), BlockVector3.at(x + 1, y + 10, z - 2));
                            region1.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
                            rm.addRegion(region1);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aLocation set!"));
                            break;
                        case SOUTH:
                            ArrayList<String> fileStringList = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");
                            String uuid = UUID.randomUUID().toString();
                            fileStringList.add(uuid);
                            mainFile.getOrSetDefault("UUIDTreeConstants", fileStringList);
                            mainFile.getOrSetDefault(uuid + ".Schematic_Name", args[0]);
                            mainFile.getOrSetDefault(uuid + ".TimerStarted", false);

                            mainFile.getOrSetDefault(uuid + ".Direction", "SOUTH");
                            mainFile.getOrSetDefault(uuid + ".Tree_Location.X", x);
                            mainFile.getOrSetDefault(uuid + ".Tree_Location.Y", y);
                            mainFile.getOrSetDefault(uuid + ".Tree_Location.Z", z + 1);

                            ProtectedRegion region2 = new ProtectedCuboidRegion(uuid, BlockVector3.at(x + 1, y, z), BlockVector3.at(x - 1, y + 10, z + 2));
                            region2.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
                            rm.addRegion(region2);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aLocation set! &7(make sure your schematic was copied facing the south direction)"));
                            break;
                        case EAST:
                            ArrayList<String> fileStringList1 = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");
                            String uuid1 = UUID.randomUUID().toString();
                            fileStringList1.add(uuid1);
                            mainFile.getOrSetDefault("UUIDTreeConstants", fileStringList1);
                            mainFile.getOrSetDefault(uuid1 + ".Schematic_Name", args[0]);
                            mainFile.getOrSetDefault(uuid1 + ".TimerStarted", false);

                            mainFile.getOrSetDefault(uuid1 + ".Direction", "EAST");
                            mainFile.getOrSetDefault(uuid1 + ".Tree_Location.X", x + 1);
                            mainFile.getOrSetDefault(uuid1 + ".Tree_Location.Y", y);
                            mainFile.getOrSetDefault(uuid1 + ".Tree_Location.Z", z);

                            ProtectedRegion region3 = new ProtectedCuboidRegion(uuid1, BlockVector3.at(x, y, z - 1), BlockVector3.at(x + 2, y + 10, z + 1));
                            region3.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
                            rm.addRegion(region3);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aLocation set! &7(make sure your schematic was copied facing the east direction)"));
                            break;
                        case WEST:
                            ArrayList<String> fileStringList2 = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");
                            String uuid2 = UUID.randomUUID().toString();
                            fileStringList2.add(uuid2);
                            mainFile.getOrSetDefault("UUIDTreeConstants", fileStringList2);
                            mainFile.getOrSetDefault(uuid2 + ".Schematic_Name", args[0]);
                            mainFile.getOrSetDefault(uuid2 + ".TimerStarted", false);

                            mainFile.getOrSetDefault(uuid2 + ".Direction", "WEST");
                            mainFile.getOrSetDefault(uuid2 + ".Tree_Location.X", player.getLocation().getBlockX() - 1);
                            mainFile.getOrSetDefault(uuid2 + ".Tree_Location.Y", player.getLocation().getBlockY());
                            mainFile.getOrSetDefault(uuid2 + ".Tree_Location.Z", player.getLocation().getBlockZ());

                            ProtectedRegion region4 = new ProtectedCuboidRegion(uuid2, BlockVector3.at(x, y, z + 1), BlockVector3.at(x - 2, y + 10, z - 1));
                            region4.setFlag(Flags.BLOCK_BREAK, StateFlag.State.ALLOW);
                            rm.addRegion(region4);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aLocation set! &7(make sure your schematic was copied facing the west direction)"));
                            break;
                    }
                } else player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &cYou must enter the name of the &4schematic &cyou wish to use. &7(ex. large_tree) &c&lAND the area of blocks you want to check. &7(ex. 4; This will make an area 4x4)"));
            }

        return true;
    }


}