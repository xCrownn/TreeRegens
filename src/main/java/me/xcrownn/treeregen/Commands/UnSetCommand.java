package me.xcrownn.treeregen.Commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonhard.storage.Json;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class UnSetCommand implements CommandExecutor{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("removeTree")) {
            Player player = (Player) sender;
            Json mainFile = new Json("Locations", "plugins/TreeRegen");
            int size = mainFile.getInt("DO_NOT_EDIT");
            System.out.println(mainFile.getInt("DO_NOT_EDIT"));

            System.out.println("command");

            ArrayList<String> list = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");

            for (String name : list) {
                System.out.println(name);
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager rm = container.get(BukkitAdapter.adapt(player.getWorld()));
                String rgName = mainFile.getString(name + ".RegionName");
                boolean isRunning = mainFile.getBoolean(name + ".TimerStarted");

                assert rm != null;
                if (Objects.requireNonNull(rm.getRegion(rgName)).contains(player.getLocation().getBlockX(), player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ()) && !isRunning) {

                    ArrayList<String> fileStringList = (ArrayList<String>) mainFile.getStringList("UUIDTreeConstants");
                    fileStringList.remove(name);
                    mainFile.remove(name + "");
                    rm.removeRegion(rgName);
                    try {
                        rm.save();
                    } catch (StorageException e) {
                        e.printStackTrace();
                    }
                    mainFile.forceReload();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aTree successfully removed!"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &cYou are not near a set tree! (try getting closer to the trunk)"));
                }
            }
        }
        return true;
    }

}
