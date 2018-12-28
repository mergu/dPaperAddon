package net.mergu.dpaperaddon.events.player;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerChangesArmorScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // paper player changes armor (in <area>)
    //
    // @Regex ^on paper player changes armor$
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

    public static PlayerChangesArmorScriptEvent instance;

    public PlayerArmorChangeEvent event;
    private Element slot_type;
    private dItem new_item;
    private dItem old_item;

    public PlayerChangesArmorScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("paper player changes armor");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return runInCheck(scriptContainer, s, CoreUtilities.toLowerCase(s), event.getPlayer().getLocation());
    }

    @Override
    public String getName() {
        return "PaperPlayerChangesArmor";
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
                return slot_type;
            case "new_item":
                return new_item;
            case "old_item":
                return old_item;
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }

        slot_type = new Element(event.getSlotType().name());
        new_item = new dItem(event.getNewItem());
        old_item = new dItem(event.getOldItem());
        this.event = event;
        fire();
    }
}
