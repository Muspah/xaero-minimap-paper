package eu.internetpolice.minimap.event;

import eu.internetpolice.minimap.XaeroMinimapPaper;
import eu.internetpolice.minimap.entity.MapPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;

@SuppressWarnings("unused")
public class PlayerEvents implements Listener {
    protected final XaeroMinimapPaper plugin;

    public PlayerEvents(XaeroMinimapPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    protected void onPlayerRegisterChannel(PlayerRegisterChannelEvent event) {
        // Not one of our channels, abort:
        if (!event.getChannel().equalsIgnoreCase(XaeroMinimapPaper.worldmapChannel) &&
            !event.getChannel().equalsIgnoreCase(XaeroMinimapPaper.minimapChannel)) {
            return;
        }

        if (!plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            plugin.players.put(event.getPlayer().getUniqueId(), new MapPlayer(event.getPlayer(), plugin));
        }

        plugin.players.get(event.getPlayer().getUniqueId()).connectsToChannel(event.getChannel());
    }

    @EventHandler
    protected void onPlayerDisconnect(PlayerQuitEvent event) {
        if (plugin.players.containsKey(event.getPlayer().getUniqueId())) {
            plugin.players.get(event.getPlayer().getUniqueId()).disconnect();
        }
    }

    @EventHandler
    protected void onPlayerMove(PlayerMoveEvent event) {
        plugin.getRadar().positionUpdate(event.getPlayer(), event.getTo());
    }

    @EventHandler
    protected void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        plugin.getRadar().playerReset(event.getPlayer());
    }
}
