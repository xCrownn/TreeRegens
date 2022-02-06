package me.xcrownn.treeregen;

import de.leonhard.storage.Yaml;
import me.xcrownn.treeregen.Commands.SetTreeCommand;
import me.xcrownn.treeregen.Events.BlockListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TreeRegen extends JavaPlugin {

    @Override
    public void onEnable() {
        File folder = new File("plugins/TreeRegen/Schematics");
        folder.mkdirs();
        getCommand("setTree").setExecutor(new SetTreeCommand(this));
        getServer().getPluginManager().registerEvents(new BlockListener(this), this);

        Yaml config = new Yaml("Config", "plugins/TreeRegen");

        config.setDefault("Regentime", 20);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
