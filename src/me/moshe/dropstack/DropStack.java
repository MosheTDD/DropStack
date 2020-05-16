package me.moshe.dropstack;

import me.moshe.dropstack.file.DroppedItemData;
import me.moshe.dropstack.listener.PlayerListener;
import me.moshe.dropstack.util.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public class DropStack extends JavaPlugin {
    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        new Utils(this);
        DroppedItemData.setup();
    }

    @Override
    public void onDisable(){
        DroppedItemData.save();
    }

}
