package net.mergu.dpaperaddon.events.server;

import com.destroystokyo.paper.event.server.ServerExceptionEvent;
import com.destroystokyo.paper.exception.ServerException;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerExceptionScriptEvent extends ScriptEvent implements Listener {

    // <--[event]
    // @Events
    // paper server exception
    //
    // @Regex ^on paper server exception$
    //
    // @Cancellable false
    //
    // @Triggers when the server throws an exception.
    //
    // @Context
    // <context.message> returns the exception's message.
    // <context.name> returns the exception's name.
    //
    // -->

    public static ServerExceptionScriptEvent instance;

    public ServerExceptionEvent event;
    private ServerException exception;

    public ServerExceptionScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.toLowerCase(s).startsWith("paper server exception");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        return true;
    }

    @Override
    public String getName() {
        return "PaperServerException";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        ServerExceptionEvent.getHandlerList().unregister(this);
    }

    @Override
    public dObject getContext(String name) {
        switch (name) {
            case "message":
                return new Element(exception.getMessage());
            case "name":
                return new Element(exception.getClass().getName());
            // TODO: Recurse into causes
            default:
                return super.getContext(name);
        }
    }

    @EventHandler
    public void onServerException(ServerExceptionEvent event) {
        exception = event.getException();
        this.event = event;
        fire();
    }
}
