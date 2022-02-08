package me.xcrownn.treeregen.Events;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import me.xcrownn.treeregen.TreeRegen;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.*;

public class BlockListener implements Listener {

    private final TreeRegen plugin;

    public BlockListener(TreeRegen plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void PlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (Tag.LOGS.isTagged(event.getBlock().getType()) || Tag.LEAVES.isTagged(event.getBlock().getType())) {

            //Grab config files
            Yaml config = new Yaml("Config", "plugins/TreeRegen");
            Json mainFile = new Json("Locations", "plugins/TreeRegen");
            int size = mainFile.getInt("DO_NOT_EDIT");
            double brokenBlockX = event.getBlock().getLocation().getBlockX();
            double brokenBlockY = event.getBlock().getLocation().getBlockY();
            double brokenBlockZ = event.getBlock().getLocation().getBlockZ();

            //loop through entries in the "Locations" file and check if the cords align with the block broken
            for (int i = 1; i <= size; i++) {
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager rm = container.get(BukkitAdapter.adapt(player.getWorld()));
                boolean isStarted = mainFile.getBoolean(i + ".TimerStarted");
                //Get base tree location
                double x = mainFile.getDouble(i + ".Tree_Location.X");
                double y = mainFile.getDouble(i + ".Tree_Location.Y");
                double z = mainFile.getDouble(i + ".Tree_Location.Z");
                String rgName = mainFile.getString(i + ".RegionName");
                //Check for

                if (!isStarted && rm.getRegion(rgName).contains(BlockVector3.at(brokenBlockX,brokenBlockY,brokenBlockZ))) {
                    int finalI = i;
                    mainFile.set(i + ".TimerStarted", true);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

                        //Get file name and location
                        File schem = new File(plugin.getDataFolder().getAbsolutePath() + "/Schematics/" + mainFile.getString(finalI + ".Schematic_Name") + ".schem");
                        Location loc = new Location(player.getWorld(), x, y, z + 1);

                        //Paste schematic
                        pasteSchematic(schem, loc);

                        //Set the timerstarted value back to false
                        mainFile.set(finalI + ".TimerStarted", false);

                    }, config.getInt("Regentime") * 20L);

                    }
                }
            }
        }

    public void pasteSchematic(File schematic, Location location) {
        if (schematic == null) throw new NullPointerException();

        Clipboard clipboard = null;
        ClipboardFormat format = ClipboardFormats.findByFile(schematic);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematic))) {
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (clipboard == null) throw new NullPointerException();

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }
    }
}