package net.mergu.dpaperaddon.events.server;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.aH;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.CachedServerIcon;

import java.io.File;
import java.util.List;

public class PaperServerListPingScriptEvent extends ScriptEvent implements Listener {

    // <--[event]
    // @Events
    // paper server list ping
    //
    // @Regex ^on paper server list ping$
    //
    // @Cancellable true
    //
    // @Triggers when the server is pinged for a client's server list.
    //
    // @Context
    // <context.motd> returns the MOTD that will show.
    // <context.max_players> returns the number of max players that will show.
    // <context.num_players> returns the number of online players that will show.
    // <context.address> returns the IP address requesting the list.
    // <context.version> returns the server version sent to the client.
    //
    // @Determine
    // "MOTD:" + Element to change the message of the day.
    // "NUM_PLAYERS:" + Element(Number) to change the number of displayed players.
    // "MAX_PLAYERS:" + Element(Number) to change the number of displayed maximum players.
    // "VERSION:" + Element to change the server version sent to the client.
    // "ICON:" + Element to change the server icon sent to the client by filename. Icon must be 64x64 pixels.
    // "HOVER_TEXT:" + dList to change the text displayed when hovering over the player count.
    // "HIDE_PLAYERS" to hide all player related information. This will make the player count "???"
    //
    // -->

    public static PaperServerListPingScriptEvent instance;

    public PaperServerListPingEvent event;
    private Element motd;
    private Element max_players;
    private Element num_players;
    private Element address;
    private Element version;
    private dList hover_text;
    private CachedServerIcon icon;
    private boolean hide_players;

    public PaperServerListPingScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("paper server list ping");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "PaperServerListPing";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        PaperServerListPingEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        String lower = CoreUtilities.toLowerCase(determination);
        if (lower.equals("hide_players")) {
            hide_players = true;
            return true;
        }

        String value = lower.substring(lower.indexOf(':') + 1);

        if (lower.startsWith("motd:")) {
            motd = new Element(value);
            return true;
        }

        if (lower.startsWith("num_players:") && aH.matchesInteger(value)) {
            num_players = new Element(value);
            return true;
        }

        if (lower.startsWith("max_players:") && aH.matchesInteger(value)) {
            max_players = new Element(value);
            return true;
        }

        if (lower.startsWith("version:")) {
            version = new Element(value);
            return true;
        }

        if (lower.startsWith("icon:")) {
            try {
                File f = new File(Bukkit.getWorldContainer().getAbsolutePath(), value);
                icon = Bukkit.getServer().loadServerIcon(f);
            }
            catch (Exception e) {
                dB.echoError("Unable to load icon file \"" + value + "\". Make sure this is a valid server icon.");
                return false;
            }

            return true;
        }

        if (lower.startsWith("hover_text:")) {
            hover_text = dList.valueOf(value);
            return true;
        }

        return super.applyDetermination(container, determination);
    }

    @Override
    public dObject getContext(String name) {
        switch (name) {
            case "motd":
                return motd;
            case "max_players":
                return max_players;
            case "num_players":
                return num_players;
            case "address":
                return address;
            case "version":
                return version;
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        motd = new Element(event.getMotd());
        max_players = new Element(event.getMaxPlayers());
        num_players = new Element(event.getNumPlayers());
        address = new Element(event.getAddress().toString());
        version = new Element(event.getVersion());
        hover_text = null;
        icon = null;
        hide_players = false;

        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
        if (cancelled) return;

        event.setMotd(motd.asString());
        event.setNumPlayers(num_players.asInt());
        event.setMaxPlayers(max_players.asInt());
        event.setVersion(version.asString());
        event.setHidePlayers(hide_players);

        if (hover_text != null) {
            List<PlayerProfile> profiles = event.getPlayerSample();
            profiles.clear();
            for (String line : hover_text) {
                profiles.add(Bukkit.createProfile(line));
            }
        }

        if (icon != null) {
            event.setServerIcon(icon);
        }
    }
}