package net.mergu.dpaperaddon.events.player;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSpectateScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // paper player starts spectating (<entity>) (in <area>)
    // paper player stops spectating (<entity>) (in <area>)
    //
    // @Regex TODO
    //
    // @Cancellable true
    //
    // @Triggers when a player starts or stops spectating an entity.
    //
    // @Context
    // <context.new_entity> returns the newly spectated dEntity.
    // <context.old_entity> returns the previously spectated dEntity.
    //
    // -->

    public PlayerSpectateScriptEvent() {
        instance = this;
    }

    public static PlayerSpectateScriptEvent instance;

    private PlayerEvent event;
    private dEntity old_entity;
    private dEntity new_entity;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("paper player starts spectating") || lower.startsWith("paper player stops spectating");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);

        boolean start = CoreUtilities.getXthArg(2, lower).equals("starts");

        if ((start && event instanceof PlayerStopSpectatingEntityEvent)
                || (!start && event instanceof PlayerStartSpectatingEntityEvent)) {
            return false;
        }

        dEntity entity = start ? new_entity : old_entity;
        String arg = CoreUtilities.getXthArg(4, lower);

        if (!arg.isEmpty() && !arg.equals("in") && !tryEntity(entity, arg)) {
            return false;
        }

        return runInCheck(scriptContainer, s, lower, entity.getLocation());
    }

    @Override
    public String getName() {
        return "PaperPlayerSpectates";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PlayerStartSpectatingEntityEvent.getHandlerList().unregister(this);
        PlayerStopSpectatingEntityEvent.getHandlerList().unregister(this);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(dEntity.isPlayer(event.getPlayer()) ? dEntity.getPlayerFrom(event.getPlayer()) : null, null);
    }

    @Override
    public dObject getContext(String name) {
        switch (name) {
            case "new_entity":
                return new_entity;
            case "old_entity":
                return old_entity;
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onStartSpectate(PlayerStartSpectatingEntityEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }

        old_entity = new dEntity(event.getCurrentSpectatorTarget());
        new_entity = new dEntity(event.getNewSpectatorTarget());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }

    @EventHandler
    public void onStopSpectate(PlayerStopSpectatingEntityEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }

        old_entity = new dEntity(event.getSpectatorTarget());
        new_entity = new dEntity(event.getPlayer());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
