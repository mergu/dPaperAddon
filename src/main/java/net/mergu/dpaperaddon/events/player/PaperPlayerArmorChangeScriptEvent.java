package net.mergu.dpaperaddon.events.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperPlayerArmorChangeScriptEvent extends ScriptEvent implements Listener {

    // <--[event]
    // @Events
    // paper player armor change
    //
    // @Regex ^on paper player armor change$
    //
    // @Cancellable false
    //
    // @Triggers when a player changes their equipped armor.
    //
    // @Context
    // <context.slot_type> returns the type of slot being altered.
    // <context.new_item> returns the new dItem.
    // <context.old_item> returns the old dItem.
    //
    // -->

    public static PaperPlayerArmorChangeScriptEvent instance;

    public PlayerArmorChangeEvent event;

    public PaperPlayerArmorChangeScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("paper player armor change");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "PaperPlayerArmorChange";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerArmorChangeEvent.getHandlerList().unregister(this);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getPlayer()), null);
    }

    @Override
    public dObject getContext(String name) {
        switch (name) {
            case "slot_type":
                return new Element(event.getSlotType().name());
            case "new_item":
                return new dItem(event.getNewItem());
            case "old_item":
                return new dItem(event.getOldItem());
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        this.event = event;
        fire();
    }
}
