package net.mergu.dpaperaddon;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.mergu.dpaperaddon.events.PlayerArmorChangeScriptEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class dPaperAddon extends JavaPlugin {
    public static dPaperAddon instance;

    @Override
    public void onEnable() {
        dB.log("dPaperAddon loaded!");
        registerCommands();
        registerEvents();
        instance = this;
    }

    private void registerCommands() {
        // TODO
    }

    private void registerEvents() {
        ScriptEvent.registerScriptEvent(new PlayerArmorChangeScriptEvent());
    }
}
