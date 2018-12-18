package net.mergu.dpaperaddon;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.mergu.dpaperaddon.events.player.PaperPlayerArmorChangeScriptEvent;
import net.mergu.dpaperaddon.events.server.PaperServerListPingScriptEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class dPaperAddon extends JavaPlugin {
    public static dPaperAddon instance;

    @Override
    public void onEnable() {
        if (!getServer().getName().equalsIgnoreCase("paper")) {
            dB.echoError("Could not enable dPaperAddon. Are you running Paper?");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        dB.log("dPaperAddon enabled!");
        registerCommands();
        registerEvents();
        instance = this;
    }

    private void registerCommands() {
        // TODO
    }

    private void registerEvents() {
        ScriptEvent.registerScriptEvent(new PaperPlayerArmorChangeScriptEvent());
        ScriptEvent.registerScriptEvent(new PaperServerListPingScriptEvent());
    }
}
