package net.mergu.dpaperaddon;

import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.mergu.dpaperaddon.events.player.PlayerArmorChangeScriptEvent;
import net.mergu.dpaperaddon.events.player.PlayerSpectateScriptEvent;
import net.mergu.dpaperaddon.events.server.ServerExceptionScriptEvent;
import net.mergu.dpaperaddon.events.server.ServerListPingScriptEvent;
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
        ScriptEvent.registerScriptEvent(new PlayerArmorChangeScriptEvent());
        ScriptEvent.registerScriptEvent(new PlayerSpectateScriptEvent());
        ScriptEvent.registerScriptEvent(new ServerExceptionScriptEvent());
        ScriptEvent.registerScriptEvent(new ServerListPingScriptEvent());
    }
}
