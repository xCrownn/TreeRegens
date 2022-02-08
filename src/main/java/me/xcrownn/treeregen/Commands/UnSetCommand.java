package me.xcrownn.treeregen.Commands;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
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
import org.jetbrains.annotations.NotNull;

public class UnSetCommand implements CommandExecutor{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (label.equalsIgnoreCase("removeTree")) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            Json mainFile = new Json("Locations", "plugins/TreeRegen");
            int size = mainFile.getInt("DO_NOT_EDIT");
            for (int i = 1; i == size; i++) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager rm = container.get(BukkitAdapter.adapt(player.getWorld()));
                System.out.println(rm.getApplicableRegionsIDs(BukkitAdapter.asBlockVector(location)));
                System.out.println(i);
                String rgName = mainFile.getString(i + ".RegionName");
                boolean isRunning = mainFile.getBoolean(i + ".TimerStarted");

                if (rm.getRegion(rgName).contains(player.getLocation().getBlockX(), player.getLocation().getBlockY() + 1, player.getLocation().getBlockZ()) && !isRunning) {
                    System.out.println("Removing tree...");
                    mainFile.remove(i + "");
                    rm.removeRegion(rgName);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &aTree successfully removed!"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&2&lTreeRegen&7]&r &cYou are not near a set tree! (try getting closer to the trunk)"));
                }
            }
        }
        return true;
    }

}
